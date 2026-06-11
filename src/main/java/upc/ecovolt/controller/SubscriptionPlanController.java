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
import upc.ecovolt.mapping.dto.SubscriptionPlanDto;
import upc.ecovolt.service.SubscriptionPlanService;
import upc.ecovolt.util.AppSettings;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Subscription Plans", description = "Endpoints para la gestión de monetización y límites SaaS")
@RestController
@RequestMapping("/api/v1/plans")
@RequiredArgsConstructor
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class SubscriptionPlanController {

    private final SubscriptionPlanService planService;

    // --- ACCIONES ADMINISTRATIVAS CON NOTIFICACIÓN ---

    @Operation(summary = "Crear un nuevo plan", description = "RESTRICTED: Solo ADMIN o MANAGER. Define precios y límites de dispositivos.")
    @ApiResponse(responseCode = "201", description = "Plan creado exitosamente")
    @PostMapping
    public ResponseEntity<ApiResponseDto<SubscriptionPlanDto.Response>> create(@Valid @RequestBody SubscriptionPlanDto.Request request) {
        var data = planService.savePlan(request);

        return new ResponseEntity<>(ApiResponseDto.<SubscriptionPlanDto.Response>builder()
                .title("¡Nuevo Plan Creado!")
                .message("El plan comercial '" + data.getName() + "' ha sido registrado exitosamente.")
                .status("SUCCESS")
                .data(data)
                .build(), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un plan existente", description = "RESTRICTED: Solo ADMIN o MANAGER. Modifica reglas y costos.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<SubscriptionPlanDto.Response>> update(
            @Parameter(description = "ID del plan a modificar", example = "2") @PathVariable Integer id,
            @Valid @RequestBody SubscriptionPlanDto.Request request) {

        var data = planService.updatePlan(id, request);

        return ResponseEntity.ok(ApiResponseDto.<SubscriptionPlanDto.Response>builder()
                .title("Plan Actualizado")
                .message("Las condiciones y límites del plan '" + data.getName() + "' se han modificado correctamente.")
                .status("SUCCESS")
                .data(data)
                .build());
    }

    @Operation(summary = "Eliminar un plan", description = "RESTRICTED: Solo ADMIN. Retira el plan del catálogo.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> delete(
            @Parameter(description = "ID del plan a eliminar", example = "3") @PathVariable Integer id) {

        planService.delete(id);

        return ResponseEntity.ok(ApiResponseDto.<Void>builder()
                .title("Plan Eliminado")
                .message("El plan de suscripción ha sido retirado del sistema de forma permanente.")
                .status("SUCCESS")
                .build());
    }

    // --- CONSULTAS DE DATOS DIRECTOS ---

    @Operation(summary = "Obtener todos los planes activos", description = "ACCESO PÚBLICO")
    @GetMapping
    public ResponseEntity<List<SubscriptionPlanDto.Response>> getAll() {
        return ResponseEntity.ok(planService.findAllPlans());
    }

    @Operation(summary = "Obtener un plan por ID", description = "ACCESO PÚBLICO")
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionPlanDto.Response> getById(@PathVariable Integer id) {
        return planService.findPlanById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Filtrar por rango de precio")
    @GetMapping("/filter-price")
    public ResponseEntity<List<SubscriptionPlanDto.Response>> getByPriceRange(
            @RequestParam BigDecimal min, @RequestParam BigDecimal max) {
        return ResponseEntity.ok(planService.findPlansByPriceRange(min, max));
    }

    @Operation(summary = "Métrica SaaS: Usuarios por plan")
    @GetMapping("/{id}/active-users")
    public ResponseEntity<Long> getActiveUserCount(@PathVariable Integer id) {
        return ResponseEntity.ok(planService.countActiveUsersByPlan(id));
    }
}
