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
import upc.ecovolt.mapping.dto.ApiResponseDto; // Importante
import upc.ecovolt.mapping.dto.energygoaldto.EnergyGoalRequestDto;
import upc.ecovolt.mapping.dto.energygoaldto.EnergyGoalResponseDto;
import upc.ecovolt.service.EnergyGoalService;
import upc.ecovolt.util.AppSettings;

import java.util.List;

@Tag(name = "Energy Goals", description = "Endpoints para la gestión de presupuestos energéticos y alertas inteligentes")
@RestController
@RequestMapping("/api/v1/energy-goals")
@RequiredArgsConstructor
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class EnergyGoalController {

    private final EnergyGoalService goalService;

    // --- CONFIGURACIÓN DE METAS CON NOTIFICACIÓN ---

    @Operation(summary = "Establecer nueva meta de ahorro",
            description = "Define un límite mensual de kWh. El sistema validará que la vivienda pertenezca al usuario.")
    @ApiResponse(responseCode = "201", description = "Meta de ahorro creada")
    @PostMapping
    public ResponseEntity<ApiResponseDto<EnergyGoalResponseDto>> create(@Valid @RequestBody EnergyGoalRequestDto request) {
        var data = goalService.save(request);

        return new ResponseEntity<>(ApiResponseDto.<EnergyGoalResponseDto>builder()
                .title("¡Meta Establecida!")
                .message("Has configurado un límite de " + data.getMonthlyLimitKwh() + " kWh para '" + data.getHomeAlias() + "'.")
                .status("SUCCESS")
                .data(data)
                .build(), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar presupuesto o alertas", description = "Permite modificar el límite de consumo o el umbral de aviso (%).")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<EnergyGoalResponseDto>> update(
            @Parameter(description = "ID de la meta", example = "1") @PathVariable Integer id,
            @Valid @RequestBody EnergyGoalRequestDto request) {

        var data = goalService.update(id, request);

        return ResponseEntity.ok(ApiResponseDto.<EnergyGoalResponseDto>builder()
                .title("Presupuesto Actualizado")
                .message("Los ajustes de ahorro se guardaron correctamente para esta vivienda.")
                .status("SUCCESS")
                .data(data)
                .build());
    }

    @Operation(summary = "Eliminar meta de ahorro")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> delete(
            @Parameter(description = "ID de la meta a eliminar", example = "1") @PathVariable Integer id) {

        goalService.delete(id);

        return ResponseEntity.ok(ApiResponseDto.<Void>builder()
                .title("Meta Eliminada")
                .message("El presupuesto ha sido removido del sistema.")
                .status("SUCCESS")
                .build());
    }

    // --- CONSULTAS DE INTELIGENCIA (DATA DIRECTA) ---

    @Operation(summary = "Obtener meta por ID", description = "ACCESO POR PROPIEDAD: Detalles del presupuesto.")
    @GetMapping("/{id}")
    public ResponseEntity<EnergyGoalResponseDto> getById(@PathVariable Integer id) {
        return goalService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar metas por vivienda", description = "Obtiene los presupuestos activos para una propiedad específica.")
    @GetMapping("/home/{homeId}")
    public ResponseEntity<List<EnergyGoalResponseDto>> getByHome(
            @Parameter(description = "ID de la vivienda", example = "1") @PathVariable Long homeId) {
        return ResponseEntity.ok(goalService.findActiveGoalsByHome(homeId));
    }

    @Operation(summary = "Reporte de metas críticas (Admin/Analyst)",
            description = "RESTRICTED: Identifica hogares con alto riesgo de exceder su cuota energética.")
    @GetMapping("/critical")
    public ResponseEntity<List<EnergyGoalResponseDto>> getCritical(
            @Parameter(description = "Umbral de alerta mínimo (%)", example = "85") @RequestParam Integer threshold) {
        return ResponseEntity.ok(goalService.findCriticalGoals(threshold));
    }

    @Operation(summary = "Listar todas las metas (Auditoría)", description = "ADMIN/AUDITOR ONLY")
    @GetMapping
    public ResponseEntity<List<EnergyGoalResponseDto>> getAll() {
        return ResponseEntity.ok(goalService.findAll());
    }
}