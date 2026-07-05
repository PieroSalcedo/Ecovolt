package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.entity.EnergyGoal;
import upc.ecovolt.entity.Home;
import upc.ecovolt.mapping.dto.EnergyAdvisorDto;
import upc.ecovolt.repository.EnergyGoalRepository;
import upc.ecovolt.repository.EnergyReadingRepository;
import upc.ecovolt.repository.HomeRepository;
import upc.ecovolt.security.UsuarioPrincipal;
import upc.ecovolt.service.EnergyAdvisorService;
import upc.ecovolt.service.GeminiAdvisorService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnergyAdvisorServiceImpl implements EnergyAdvisorService {

    private static final BigDecimal DEFAULT_SAVING_FACTOR = new BigDecimal("0.08");

    private final HomeRepository homeRepository;
    private final EnergyGoalRepository goalRepository;
    private final EnergyReadingRepository readingRepository;
    private final GeminiAdvisorService geminiAdvisorService;

    @Override
    @Transactional(readOnly = true)
    public EnergyAdvisorDto.Response analyze(EnergyAdvisorDto.Request request) {
        if (request.getIdHome() == null || request.getIdHome() < 1) {
            throw new RuntimeException("Debe seleccionar una vivienda para analizar.");
        }

        Home home = homeRepository.findById(request.getIdHome())
                .orElseThrow(() -> new RuntimeException("Vivienda no encontrada."));
        validateHomeOwnership(home);

        DateRange range = resolveRange(request.getPeriod());
        BigDecimal totalKwh = safeDecimal(readingRepository
                .sumTotalConsumptionByHome(home.getIdHome(), range.start(), range.end()))
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal estimatedCost = totalKwh
                .multiply(home.getEnergyTariff() == null ? BigDecimal.ZERO : home.getEnergyTariff())
                .setScale(2, RoundingMode.HALF_UP);

        EnergyGoal activeGoal = goalRepository.findActiveGoalsByHome(home.getIdHome())
                .stream()
                .findFirst()
                .orElse(null);
        BigDecimal goalKwh = activeGoal == null ? null : activeGoal.getMonthlyLimitKwh();
        BigDecimal progress = calculateProgress(totalKwh, goalKwh);

        List<Object[]> topDevices = readingRepository.findTopDeviceConsumptionByHome(
                home.getIdHome(), range.start(), range.end(), PageRequest.of(0, 3));
        List<Object[]> topRooms = readingRepository.findTopRoomConsumptionByHome(
                home.getIdHome(), range.start(), range.end(), PageRequest.of(0, 3));

        List<EnergyAdvisorDto.Metric> metrics = buildMetrics(totalKwh, estimatedCost, goalKwh, progress, topDevices, topRooms);
        String fallbackRisk = resolveRiskLevel(totalKwh, progress, activeGoal);
        BigDecimal fallbackSaving = estimatedCost.multiply(DEFAULT_SAVING_FACTOR).setScale(2, RoundingMode.HALF_UP);
        List<EnergyAdvisorDto.Recommendation> fallbackRecommendations =
                buildFallbackRecommendations(home, totalKwh, estimatedCost, goalKwh, progress, topDevices, topRooms);

        String context = buildGeminiContext(home, request.getPeriod(), totalKwh, estimatedCost, goalKwh, progress, topDevices, topRooms);
        var aiPayload = geminiAdvisorService.generateAdvice(context, metrics);

        if (aiPayload.isPresent()) {
            var payload = aiPayload.get();
            return EnergyAdvisorDto.Response.builder()
                    .summary(payload.getSummary())
                    .riskLevel(payload.getRiskLevel() == null ? fallbackRisk : payload.getRiskLevel())
                    .totalKwh(totalKwh)
                    .estimatedCost(estimatedCost)
                    .monthlyGoalKwh(goalKwh)
                    .goalProgressPercentage(progress)
                    .estimatedSaving(payload.getEstimatedSaving() == null ? fallbackSaving : payload.getEstimatedSaving())
                    .aiGenerated(true)
                    .metrics(metrics)
                    .recommendations(payload.getRecommendations() == null ? fallbackRecommendations : payload.getRecommendations())
                    .build();
        }

        return EnergyAdvisorDto.Response.builder()
                .summary(buildFallbackSummary(home, totalKwh, estimatedCost, goalKwh, progress))
                .riskLevel(fallbackRisk)
                .totalKwh(totalKwh)
                .estimatedCost(estimatedCost)
                .monthlyGoalKwh(goalKwh)
                .goalProgressPercentage(progress)
                .estimatedSaving(fallbackSaving)
                .aiGenerated(false)
                .metrics(metrics)
                .recommendations(fallbackRecommendations)
                .build();
    }

    private void validateHomeOwnership(Home home) {
        var principal = (UsuarioPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = principal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !home.getUser().getIdUser().equals(principal.getIdUser())) {
            log.error("SMART ADVISOR: Usuario {} intento analizar vivienda ajena {}",
                    principal.getLogin(), home.getIdHome());
            throw new RuntimeException("Acceso denegado: No tienes permisos sobre esta vivienda.");
        }
    }

    private DateRange resolveRange(String period) {
        LocalDateTime now = LocalDateTime.now();
        String normalized = period == null ? "MONTHLY" : period.toUpperCase();

        return switch (normalized) {
            case "DAILY" -> new DateRange(LocalDate.now().atStartOfDay(), now);
            case "WEEKLY" -> new DateRange(now.minusDays(7), now);
            default -> new DateRange(LocalDate.now().withDayOfMonth(1).atStartOfDay(), now);
        };
    }

    private BigDecimal calculateProgress(BigDecimal totalKwh, BigDecimal goalKwh) {
        if (goalKwh == null || goalKwh.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }
        return totalKwh.multiply(new BigDecimal("100"))
                .divide(goalKwh, 2, RoundingMode.HALF_UP);
    }

    private String resolveRiskLevel(BigDecimal totalKwh, BigDecimal progress, EnergyGoal goal) {
        if (progress != null && goal != null) {
            if (progress.compareTo(new BigDecimal("100")) >= 0) return "HIGH";
            if (progress.compareTo(new BigDecimal(goal.getAlertThresholdPercentage())) >= 0) return "MEDIUM";
            return "LOW";
        }

        if (totalKwh.compareTo(new BigDecimal("150")) >= 0) return "HIGH";
        if (totalKwh.compareTo(new BigDecimal("80")) >= 0) return "MEDIUM";
        return "LOW";
    }

    private List<EnergyAdvisorDto.Metric> buildMetrics(BigDecimal totalKwh, BigDecimal estimatedCost,
                                                       BigDecimal goalKwh, BigDecimal progress,
                                                       List<Object[]> topDevices, List<Object[]> topRooms) {
        List<EnergyAdvisorDto.Metric> metrics = new ArrayList<>();
        metrics.add(metric("Consumo total", "HOME", totalKwh, "kWh"));
        metrics.add(metric("Costo estimado", "COST", estimatedCost, "S/"));
        if (goalKwh != null) metrics.add(metric("Meta mensual", "GOAL", goalKwh, "kWh"));
        if (progress != null) metrics.add(metric("Avance de meta", "GOAL", progress, "%"));
        topDevices.forEach(row -> metrics.add(metric(String.valueOf(row[0]), "DEVICE", toBigDecimal(row[1]), "kWh")));
        topRooms.forEach(row -> metrics.add(metric(String.valueOf(row[0]), "ROOM", toBigDecimal(row[1]), "kWh")));
        return metrics;
    }

    private List<EnergyAdvisorDto.Recommendation> buildFallbackRecommendations(Home home, BigDecimal totalKwh,
                                                                               BigDecimal estimatedCost, BigDecimal goalKwh,
                                                                               BigDecimal progress, List<Object[]> topDevices,
                                                                               List<Object[]> topRooms) {
        List<EnergyAdvisorDto.Recommendation> recommendations = new ArrayList<>();

        if (progress != null && progress.compareTo(new BigDecimal("100")) >= 0) {
            recommendations.add(recommendation("Meta mensual superada",
                    "El consumo actual ya paso la meta definida para " + home.getAlias() + ". Prioriza revisar los equipos de mayor consumo.",
                    "HIGH", "META"));
        } else if (progress != null && progress.compareTo(new BigDecimal("80")) >= 0) {
            recommendations.add(recommendation("Consumo cerca del limite",
                    "La vivienda esta cerca del umbral de alerta. Conviene reducir horarios de uso no esenciales.",
                    "MEDIUM", "ALERTA"));
        }

        if (!topDevices.isEmpty()) {
            recommendations.add(recommendation("Revisar dispositivo principal",
                    "El dispositivo con mayor consumo es " + topDevices.get(0)[0] + ". Configura horarios o modo ahorro si esta disponible.",
                    "HIGH", "DISPOSITIVO"));
        }

        if (!topRooms.isEmpty()) {
            recommendations.add(recommendation("Optimizar ambiente critico",
                    "El ambiente con mayor consumo es " + topRooms.get(0)[0] + ". Revisa equipos encendidos por periodos prolongados.",
                    "MEDIUM", "AHORRO"));
        }

        if (recommendations.isEmpty()) {
            recommendations.add(recommendation("Mantener consumo eficiente",
                    "El consumo actual no muestra alertas fuertes. Mantener revisiones semanales ayuda a sostener el ahorro.",
                    "LOW", "AHORRO"));
        }

        return recommendations;
    }

    private String buildFallbackSummary(Home home, BigDecimal totalKwh, BigDecimal estimatedCost,
                                        BigDecimal goalKwh, BigDecimal progress) {
        String goalText = goalKwh == null
                ? "No hay una meta mensual activa para comparar."
                : "La meta mensual es " + goalKwh + " kWh y el avance actual es " + progress + "%.";
        return "La vivienda " + home.getAlias() + " registra " + totalKwh + " kWh con costo estimado de S/ "
                + estimatedCost + ". " + goalText;
    }

    private String buildGeminiContext(Home home, String period, BigDecimal totalKwh, BigDecimal estimatedCost,
                                      BigDecimal goalKwh, BigDecimal progress, List<Object[]> topDevices,
                                      List<Object[]> topRooms) {
        return "Vivienda: " + home.getAlias()
                + ". Ciudad: " + home.getCity()
                + ". Periodo: " + (period == null ? "MONTHLY" : period)
                + ". Consumo total: " + totalKwh + " kWh"
                + ". Costo estimado: S/ " + estimatedCost
                + ". Meta mensual: " + (goalKwh == null ? "sin meta" : goalKwh + " kWh")
                + ". Avance de meta: " + (progress == null ? "sin avance" : progress + "%")
                + ". Dispositivo principal: " + firstLabel(topDevices)
                + ". Ambiente principal: " + firstLabel(topRooms) + ".";
    }

    private String firstLabel(List<Object[]> rows) {
        return rows.isEmpty() ? "sin datos" : String.valueOf(rows.get(0)[0]);
    }

    private EnergyAdvisorDto.Metric metric(String label, String type, BigDecimal value, String unit) {
        return EnergyAdvisorDto.Metric.builder()
                .label(label)
                .type(type)
                .value(value == null ? BigDecimal.ZERO : value.setScale(2, RoundingMode.HALF_UP))
                .unit(unit)
                .build();
    }

    private EnergyAdvisorDto.Recommendation recommendation(String title, String description,
                                                           String priority, String category) {
        return EnergyAdvisorDto.Recommendation.builder()
                .title(title)
                .description(description)
                .priority(priority)
                .category(category)
                .build();
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value instanceof BigDecimal decimal) {
            return decimal;
        }
        if (value instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue());
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal safeDecimal(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private record DateRange(LocalDateTime start, LocalDateTime end) {
    }
}
