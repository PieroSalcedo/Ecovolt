package upc.ecovolt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upc.ecovolt.mapping.dto.ApiResponseDto;
import upc.ecovolt.mapping.dto.SubscriptionPlanDto;
import upc.ecovolt.service.SubscriptionPlanService;
import upc.ecovolt.util.WebUtil;
import upc.ecovolt.util.AppSettings;

import java.util.List;

@Tag(name = "Subscription Plans", description = "Gestión de monetización y límites de dispositivos (SaaS)")
@RestController
@RequestMapping("/api/v1/plans")
@RequiredArgsConstructor
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class SubscriptionPlanController {

    private final SubscriptionPlanService planService;

    // --- ACCIONES ADMINISTRATIVAS ---

    @PostMapping
    @Operation(summary = "Crear nuevo plan", description = "Solo ADMIN/MANAGER. Define el límite de equipos y precio.")
    public ResponseEntity<ApiResponseDto<SubscriptionPlanDto.Response>> create(@RequestBody SubscriptionPlanDto.Request request) {
        var data = planService.savePlan(request);
        return WebUtil.created(data, "Plan comercial '" + data.getName() + "' registrado.");
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar plan")
    public ResponseEntity<ApiResponseDto<SubscriptionPlanDto.Response>> update(
            @PathVariable Integer id, @RequestBody SubscriptionPlanDto.Request request) {
        var data = planService.updatePlan(id, request);
        return WebUtil.ok(data, "Condiciones del plan actualizadas.");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar plan", description = "Borrado lógico para no afectar usuarios actuales.")
    public ResponseEntity<ApiResponseDto<Void>> delete(@PathVariable Integer id) {
        planService.delete(id);
        return WebUtil.ok(null, "El plan ha sido retirado del catálogo.");
    }

    // --- CONSULTAS PÚBLICAS Y ANALÍTICAS ---

    @GetMapping
    @Operation(summary = "Listar todos los planes", description = "Usado para mostrar la tabla de precios en el registro.")
    public ResponseEntity<ApiResponseDto<List<SubscriptionPlanDto.Response>>> getAll() {
        var data = planService.findAllPlans();
        return WebUtil.ok(data, "Catálogo de planes cargado.");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalle del plan")
    public ResponseEntity<ApiResponseDto<SubscriptionPlanDto.Response>> getById(@PathVariable Integer id) {
        return planService.findPlanById(id)
                .map(plan -> WebUtil.ok(plan, "Detalle del plan"))
                .orElseThrow(() -> new RuntimeException("Plan no encontrado"));
    }

    @GetMapping("/upgrade-options/{currentLimit}")
    @Operation(summary = "Opciones de mejora", description = "Busca planes superiores al actual del usuario.")
    public ResponseEntity<ApiResponseDto<List<SubscriptionPlanDto.Response>>> getUpgrades(@PathVariable Integer currentLimit) {
        var data = planService.findUpgradeOptions(currentLimit);
        return WebUtil.ok(data, "Opciones de escalabilidad encontradas.");
    }

    @GetMapping("/{id}/active-users")
    @Operation(summary = "Métrica SaaS", description = "Cuenta cuántos usuarios están suscritos a este plan.")
    public ResponseEntity<ApiResponseDto<Long>> getActiveUsers(@PathVariable Integer id) {
        var count = planService.countActiveUsersByPlan(id);
        return WebUtil.ok(count, "Conteo de usuarios por plan completado.");
    }
}