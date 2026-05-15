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
import upc.ecovolt.mapping.dto.subscriptionplandto.SubscriptionPlanRequestDto;
import upc.ecovolt.mapping.dto.subscriptionplandto.SubscriptionPlanResponseDto;
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

    @Operation(summary = "Obtener todos los planes activos",
            description = "ACCESO PÚBLICO: Retorna la lista de planes vigentes.")
    @GetMapping
    public ResponseEntity<List<SubscriptionPlanResponseDto>> getAll() {
        return ResponseEntity.ok(planService.findAllPlans());
    }

    @Operation(summary = "Obtener un plan por ID",
            description = "ACCESO PÚBLICO: Consulta detalles técnicos de un plan.")
    @ApiResponse(responseCode = "200", description = "Plan encontrado")
    @ApiResponse(responseCode = "404", description = "Plan no existe")
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionPlanResponseDto> getById(
            @Parameter(description = "ID del plan", example = "1")
            @PathVariable Integer id) {
        return planService.findPlanById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo plan",
            description = "RESTRICTED: Solo usuarios con ROLE_ADMIN o ROLE_MANAGER.")
    @ApiResponse(responseCode = "201", description = "Plan creado")
    @ApiResponse(responseCode = "403", description = "Prohibido: No tienes permisos de administrador")
    @PostMapping
    public ResponseEntity<SubscriptionPlanResponseDto> create(@Valid @RequestBody SubscriptionPlanRequestDto request) {
        return new ResponseEntity<>(planService.savePlan(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un plan existente",
            description = "RESTRICTED: Solo usuarios con ROLE_ADMIN o ROLE_MANAGER.")
    @ApiResponse(responseCode = "200", description = "Plan actualizado")
    @ApiResponse(responseCode = "403", description = "Prohibido: Permisos insuficientes")
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionPlanResponseDto> update(
            @Parameter(description = "ID del plan a modificar", example = "2")
            @PathVariable Integer id,
            @Valid @RequestBody SubscriptionPlanRequestDto request) {
        return ResponseEntity.ok(planService.updatePlan(id, request));
    }

    @Operation(summary = "Eliminar un plan",
            description = "RESTRICTED: Solo accesible por el ROLE_ADMIN.")
    @ApiResponse(responseCode = "204", description = "Plan eliminado con éxito")
    @ApiResponse(responseCode = "403", description = "Prohibido: Solo el Administrador puede borrar planes")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del plan a eliminar", example = "3")
            @PathVariable Integer id) {
        planService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --- ENDPOINTS DE INTELIGENCIA DE NEGOCIO ---

    @Operation(summary = "Filtrar planes por rango de precio",
            description = "ACCESO PÚBLICO: Filtra planes activos por presupuesto.")
    @GetMapping("/filter-price")
    public ResponseEntity<List<SubscriptionPlanResponseDto>> getByPriceRange(
            @Parameter(description = "Precio mínimo", example = "10.00") @RequestParam BigDecimal min,
            @Parameter(description = "Precio máximo", example = "50.00") @RequestParam BigDecimal max) {
        return ResponseEntity.ok(planService.findPlansByPriceRange(min, max));
    }

    @Operation(summary = "Filtrar por nivel de soporte",
            description = "ACCESO PÚBLICO: Busca planes por su etiqueta de catálogo (Ej: 'Premium').")
    @GetMapping("/filter-support")
    public ResponseEntity<List<SubscriptionPlanResponseDto>> getBySupport(
            @Parameter(description = "Nombre del nivel", example = "Premium")
            @RequestParam String supportLevel) {
        return ResponseEntity.ok(planService.findBySupportLevelName(supportLevel));
    }

    @Operation(summary = "Métrica SaaS: Usuarios por plan",
            description = "RESTRICTED: Solo ROLE_ADMIN y ROLE_ANALYST pueden ver datos de penetración de mercado.")
    @ApiResponse(responseCode = "200", description = "Conteo obtenido")
    @ApiResponse(responseCode = "403", description = "Acceso denegado a datos analíticos")
    @GetMapping("/{id}/active-users")
    public ResponseEntity<Long> getActiveUserCount(
            @Parameter(description = "ID del plan", example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(planService.countActiveUsersByPlan(id));
    }
}