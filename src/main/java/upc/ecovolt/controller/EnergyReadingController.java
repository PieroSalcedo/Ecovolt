package upc.ecovolt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upc.ecovolt.mapping.dto.energyreadingdto.EnergyReadingRequestDto;
import upc.ecovolt.mapping.dto.energyreadingdto.EnergyReadingResponseDto;
import upc.ecovolt.service.EnergyReadingService;
import upc.ecovolt.util.AppSettings;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Energy Readings", description = "Endpoints para la captura de telemetría IoT y generación de analítica de consumo")
@RestController
@RequestMapping("/api/v1/energy-readings")
@RequiredArgsConstructor
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class EnergyReadingController {

    private final EnergyReadingService readingService;

    @Operation(summary = "Registrar telemetría (IoT Gateway)", description = "Endpoint que reciben los sensores para enviar Wattage y Voltage en tiempo real")
    @PostMapping
    public ResponseEntity<EnergyReadingResponseDto> create(@Valid @RequestBody EnergyReadingRequestDto request) {
        return new ResponseEntity<>(readingService.saveReading(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener historial completo", description = "Retorna todas las lecturas registradas (Uso administrativo)")
    @GetMapping
    public ResponseEntity<List<EnergyReadingResponseDto>> getAll() {
        return ResponseEntity.ok(readingService.findAllReadings());
    }

    // --- ENDPOINTS DE ANALÍTICA AVANZADA (EL VALOR DE ECOVOLT) ---

    @Operation(summary = "Consumo acumulado por dispositivo", description = "Suma el wattage total de un equipo en un rango de tiempo para calcular costos")
    @GetMapping("/analytics/device/{idDevice}/sum")
    public ResponseEntity<BigDecimal> getSumByDevice(
            @Parameter(description = "ID del equipo IoT", example = "1") @PathVariable Long idDevice,
            @Parameter(description = "Fecha inicial", example = "2024-01-01T00:00:00") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @Parameter(description = "Fecha final", example = "2024-01-31T23:59:59") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(readingService.sumWattageByDeviceAndPeriod(idDevice, start, end));
    }

    @Operation(summary = "Consumo total de la vivienda", description = "Suma toda la energía consumida en una propiedad completa")
    @GetMapping("/analytics/home/{idHome}/total")
    public ResponseEntity<BigDecimal> getTotalHomeConsumption(
            @Parameter(description = "ID de la vivienda", example = "1") @PathVariable Long idHome,
            @Parameter(description = "Fecha inicial", example = "2024-05-01T00:00:00") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @Parameter(description = "Fecha final", example = "2024-05-31T23:59:59") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(readingService.sumTotalConsumptionByHome(idHome, start, end));
    }

    @Operation(summary = "Analítica por Categoría (Pie Chart)", description = "Suma el consumo de una categoría (Ej: 'Climatización') para reportes de distribución")
    @GetMapping("/analytics/home/{idHome}/category")
    public ResponseEntity<BigDecimal> getConsumptionByCategory(
            @Parameter(description = "ID de la vivienda", example = "1") @PathVariable Long idHome,
            @Parameter(description = "Nombre de la categoría del DataCatalogo", example = "Lighting") @RequestParam String categoryName,
            @Parameter(description = "Fecha inicial", example = "2024-05-01T00:00:00") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @Parameter(description = "Fecha final", example = "2024-05-31T23:59:59") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(readingService.sumConsumptionByCategory(idHome, categoryName, start, end));
    }

    @Operation(summary = "Monitor en tiempo real", description = "Obtiene las últimas lecturas registradas de un dispositivo para dashboards vivos")
    @GetMapping("/device/{idDevice}/latest")
    public ResponseEntity<List<EnergyReadingResponseDto>> getLatest(@PathVariable Long idDevice) {
        return ResponseEntity.ok(readingService.findLatestReadingsByDevice(idDevice));
    }

    @Operation(summary = "Detectar fugas / Consumo fantasma", description = "Busca lecturas que superen un umbral de ruido en periodos de inactividad")
    @GetMapping("/device/{idDevice}/abnormal")
    public ResponseEntity<List<EnergyReadingResponseDto>> getAbnormal(
            @PathVariable Long idDevice,
            @Parameter(description = "Wattage mínimo para considerar anomalía", example = "5.0") @RequestParam BigDecimal threshold) {
        return ResponseEntity.ok(readingService.findAbnormalConsumption(idDevice, threshold));
    }

    @Operation(summary = "Promedio de Voltaje", description = "Analiza la estabilidad de la red eléctrica para proteger equipos")
    @GetMapping("/device/{idDevice}/voltage-avg")
    public ResponseEntity<Double> getVoltageAvg(@PathVariable Long idDevice) {
        return ResponseEntity.ok(readingService.getAverageVoltageByDevice(idDevice));
    }
}