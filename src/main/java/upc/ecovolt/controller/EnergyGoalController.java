package upc.ecovolt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upc.ecovolt.mapping.dto.energygoaldto.EnergyGoalRequestDto;
import upc.ecovolt.mapping.dto.energygoaldto.EnergyGoalResponseDto;
import upc.ecovolt.service.EnergyGoalService;
import upc.ecovolt.util.AppSettings;

import java.util.List;

@Tag(name = "Energy Goals", description = "Endpoints para la gestión de presupuestos energéticos y configuración de alertas de consumo")
@RestController
@RequestMapping("/api/v1/energy-goals")
@RequiredArgsConstructor
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class EnergyGoalController {

    private final EnergyGoalService goalService;

    @Operation(summary = "Listar todas las metas de ahorro", description = "Retorna el historial de todos los presupuestos configurados en el sistema")
    @GetMapping
    public ResponseEntity<List<EnergyGoalResponseDto>> getAll() {
        return ResponseEntity.ok(goalService.findAll());
    }

    @Operation(summary = "Obtener meta por ID")
    @ApiResponse(responseCode = "200", description = "Meta encontrada")
    @ApiResponse(responseCode = "404", description = "Meta no existe")
    @GetMapping("/{id}")
    public ResponseEntity<EnergyGoalResponseDto> getById(
            @Parameter(description = "ID único de la meta", example = "1")
            @PathVariable Integer id) {
        return goalService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Establecer nueva meta de ahorro", description = "Define un límite mensual de kWh y un umbral de alerta para una vivienda")
    @PostMapping
    public ResponseEntity<EnergyGoalResponseDto> create(@Valid @RequestBody EnergyGoalRequestDto request) {
        return new ResponseEntity<>(goalService.save(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar presupuesto o alertas", description = "Modifica el límite de consumo o el porcentaje de aviso")
    @PutMapping("/{id}")
    public ResponseEntity<EnergyGoalResponseDto> update(
            @Parameter(description = "ID de la meta a modificar", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody EnergyGoalRequestDto request) {
        return ResponseEntity.ok(goalService.update(id, request));
    }

    @Operation(summary = "Eliminar meta de ahorro")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la meta a eliminar", example = "1")
            @PathVariable Integer id) {
        goalService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --- ENDPOINTS DE INTELIGENCIA PREVENTIVA ---

    @Operation(summary = "Listar metas vigentes por vivienda", description = "Obtiene los presupuestos activos para una propiedad específica")
    @GetMapping("/home/{homeId}")
    public ResponseEntity<List<EnergyGoalResponseDto>> getByHome(
            @Parameter(description = "ID de la vivienda", example = "1")
            @PathVariable Long homeId) {
        return ResponseEntity.ok(goalService.findActiveGoalsByHome(homeId));
    }

    @Operation(summary = "Reporte de metas críticas (Admin)", description = "Busca metas cuyo umbral de alerta sea muy alto (Ej: 90%) para monitoreo proactivo")
    @GetMapping("/critical")
    public ResponseEntity<List<EnergyGoalResponseDto>> getCritical(
            @Parameter(description = "Umbral mínimo de alerta", example = "90")
            @RequestParam Integer threshold) {
        return ResponseEntity.ok(goalService.findCriticalGoals(threshold));
    }
}
