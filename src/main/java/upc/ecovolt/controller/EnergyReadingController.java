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
import upc.ecovolt.mapping.dto.ApiResponseDto;
import upc.ecovolt.mapping.dto.energyreadingdto.EnergyReadingRequestDto;
import upc.ecovolt.mapping.dto.energyreadingdto.EnergyReadingResponseDto;
import upc.ecovolt.service.EnergyReadingService;
import upc.ecovolt.util.AppSettings;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Energy Readings", description = "Endpoints para la captura de telemetría IoT y generación de analítica avanzada (BI)")
@RestController
@RequestMapping("/api/v1/energy-readings")
@RequiredArgsConstructor
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class EnergyReadingController {

    private final EnergyReadingService readingService;

    // --- INGESTA DE DATOS (CON NOTIFICACIÓN) ---

    @Operation(summary = "Registrar telemetría (IoT Gateway)",
            description = "Recibe datos de potencia y voltaje. Valida que el sensor pertenezca al usuario.")
    @ApiResponse(responseCode = "201", description = "Lectura procesada exitosamente")
    @PostMapping
    public ResponseEntity<ApiResponseDto<EnergyReadingResponseDto>> create(@Valid @RequestBody EnergyReadingRequestDto request) {
        var data = readingService.saveReading(request);

        return new ResponseEntity<>(ApiResponseDto.<EnergyReadingResponseDto>builder()
                .title("Telemetría Recibida")
                .message("Datos del dispositivo registrados correctamente a las " + data.getCreatedAt().toLocalTime())
                .status("SUCCESS")
                .data(data)
                .build(), HttpStatus.CREATED);
    }

    // --- ENDPOINTS DE ANALÍTICA (DATA PURA PARA GRÁFICOS) ---

    @Operation(summary = "Consumo total de la vivienda",
            description = "Suma toda la energía de una casa. Usado para el gráfico principal del Dashboard.")
    @GetMapping("/analytics/home/{idHome}/total")
    public ResponseEntity<BigDecimal> getTotalHomeConsumption(
            @Parameter(description = "ID de la vivienda", example = "1") @PathVariable Long idHome,
            @Parameter(description = "Fecha inicio (ISO)", example = "2024-05-14T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @Parameter(description = "Fecha fin (ISO)", example = "2024-05-14T23:59:59")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(readingService.sumTotalConsumptionByHome(idHome, start, end));
    }

    @Operation(summary = "Distribución por Categoría (Pie Chart)",
            description = "Permite saber cuánto se gasta en 'Iluminación', 'AC', etc. (Valor estratégico para DEVIDA)")
    @GetMapping("/analytics/home/{idHome}/category")
    public ResponseEntity<BigDecimal> getConsumptionByCategory(
            @Parameter(description = "ID de la vivienda", example = "1") @PathVariable Long idHome,
            @Parameter(description = "Categoría del catálogo", example = "Climatización") @RequestParam String categoryName,
            @Parameter(description = "Fecha inicio", example = "2024-05-14T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @Parameter(description = "Fecha fin", example = "2024-05-14T23:59:59")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(readingService.sumConsumptionByCategory(idHome, categoryName, start, end));
    }

    @Operation(summary = "Detección de consumos fantasma",
            description = "Filtra lecturas que superan un umbral de ruido. Ayuda a identificar fugas eléctricas.")
    @GetMapping("/device/{idDevice}/abnormal")
    public ResponseEntity<List<EnergyReadingResponseDto>> getAbnormal(
            @Parameter(description = "ID del sensor", example = "1") @PathVariable Long idDevice,
            @Parameter(description = "Umbral de wattage (Watts)", example = "5.0") @RequestParam BigDecimal threshold) {
        return ResponseEntity.ok(readingService.findAbnormalConsumption(idDevice, threshold));
    }

    @Operation(summary = "Monitor en tiempo real", description = "Últimas lecturas para velocímetros o indicadores en vivo.")
    @GetMapping("/device/{idDevice}/latest")
    public ResponseEntity<List<EnergyReadingResponseDto>> getLatest(@PathVariable Long idDevice) {
        return ResponseEntity.ok(readingService.findLatestReadingsByDevice(idDevice));
    }

    @Operation(summary = "Estabilidad de Voltaje (Promedio)", description = "Analiza si la red eléctrica es estable (Soporte técnico).")
    @GetMapping("/device/{idDevice}/voltage-avg")
    public ResponseEntity<Double> getVoltageAvg(@PathVariable Long idDevice) {
        return ResponseEntity.ok(readingService.getAverageVoltageByDevice(idDevice));
    }

    @Operation(summary = "Historial Completo (Staff)", description = "AUDITOR ONLY: Lista todas las lecturas del sistema.")
    @GetMapping
    public ResponseEntity<List<EnergyReadingResponseDto>> getAll() {
        return ResponseEntity.ok(readingService.findAllReadings());
    }
}