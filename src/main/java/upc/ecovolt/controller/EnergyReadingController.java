package upc.ecovolt.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import upc.ecovolt.mapping.dto.*;
import upc.ecovolt.security.UsuarioPrincipal;
import upc.ecovolt.service.EnergyReadingService;
import upc.ecovolt.util.WebUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Energy Readings", description = "Controlador para la analítica de telemetría IoT")
@RestController
@RequestMapping("/api/v1/energy-readings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EnergyReadingController {

    private final EnergyReadingService readingService;

    @PostMapping
    public ResponseEntity<ApiResponseDto<EnergyReadingDto.Response>> guardarLectura(@RequestBody EnergyReadingDto.Request request) {
        var respuesta = readingService.saveReading(request);
        return WebUtil.ok(respuesta, "Telemetría registrada correctamente.");
    }

    @GetMapping("/device/{idDevice}/latest")
    public ResponseEntity<ApiResponseDto<List<EnergyReadingDto.Response>>> obtenerUltimasLecturas(
            @PathVariable("idDevice") Long idDevice,
            @RequestParam(name = "limit", defaultValue = "20") int limit) {
        var lista = readingService.findLatestReadingsByDevice(idDevice, limit);
        return WebUtil.ok(lista, "Últimas lecturas recuperadas.");
    }

    @GetMapping("/reporte/casas")
    public ResponseEntity<List<ReporteCasaDTO>> getReporteCasas() {
        UsuarioPrincipal principal = (UsuarioPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Ahora que el Service tiene el método, esto compilará bien
        return ResponseEntity.ok(readingService.reporteConsumoPorCasa(principal.getIdUser()));
    }

    @GetMapping("/reporte/cuartos/{idHome}")
    public ResponseEntity<List<ReporteCuartoDTO>> getReporteCuartos(@PathVariable Long idHome) {
        return ResponseEntity.ok(readingService.reporteConsumoPorCuarto(idHome));
    }

    @GetMapping("/reporte/dispositivos/{idRoom}")
    public ResponseEntity<List<ReporteDispositivoDTO>> getReporteDispositivos(@PathVariable Long idRoom) {
        // No necesitamos el principal aquí porque el filtro es por ID de cuarto directo
        List<ReporteDispositivoDTO> lista = readingService.reporteConsumoPorDispositivo(idRoom);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/device/{idDevice}/abnormal")
    public ResponseEntity<ApiResponseDto<List<EnergyReadingDto.Response>>> detectarConsumoAnormal(
            @PathVariable("idDevice") Long idDevice,
            @RequestParam(name = "threshold") BigDecimal threshold) {
        var lista = readingService.findAbnormalConsumption(idDevice, threshold);
        return WebUtil.ok(lista, "Reporte de anomalías completado.");
    }

    @GetMapping("/home/{idHome}/total")
    public ResponseEntity<ApiResponseDto<BigDecimal>> obtenerConsumoTotalVivienda(
            @PathVariable("idHome") Long idHome,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        var total = readingService.sumTotalConsumptionByHome(idHome, start, end);
        return WebUtil.ok(total, "Consumo acumulado calculado.");
    }

    @GetMapping("/device/{idDevice}/voltage-average")
    public ResponseEntity<ApiResponseDto<Double>> obtenerPromedioVoltaje(@PathVariable("idDevice") Long idDevice) {
        var promedio = readingService.getAverageVoltageByDevice(idDevice);
        return WebUtil.ok(promedio, "Voltaje promedio de red calculado.");
    }
}