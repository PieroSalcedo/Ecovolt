package upc.ecovolt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Operation(summary = "Obtener todos los planes activos", description = "Retorna la lista de planes que tienen status = 1")
    @GetMapping
    public ResponseEntity<List<SubscriptionPlanResponseDto>> getAll() {
        return ResponseEntity.ok(planService.findAllPlans());
    }

    @Operation(summary = "Obtener un plan por ID", description = "Busca los detalles de un plan específico")
    @ApiResponse(responseCode = "200", description = "Plan encontrado")
    @ApiResponse(responseCode = "404", description = "Plan no existe")
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionPlanResponseDto> getById(
            @Parameter(description = "ID del plan a consultar", example = "1")
            @PathVariable Integer id) {
        return planService.findPlanById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo nivel de suscripción", description = "Registra un plan definiendo precios y límites de dispositivos")
    @PostMapping
    public ResponseEntity<SubscriptionPlanResponseDto> create(@Valid @RequestBody SubscriptionPlanRequestDto request) {
        return new ResponseEntity<>(planService.savePlan(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un plan existente", description = "Modifica las reglas de negocio de un plan")
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionPlanResponseDto> update(
            @Parameter(description = "ID del plan a modificar", example = "2")
            @PathVariable Integer id,
            @Valid @RequestBody SubscriptionPlanRequestDto request) {
        return ResponseEntity.ok(planService.updatePlan(id, request));
    }

    @Operation(summary = "Eliminar un plan", description = "Realiza un borrado físico/lógico según la configuración")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del plan a eliminar", example = "3")
            @PathVariable Integer id) {
        planService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --- ENDPOINTS DE INTELIGENCIA DE NEGOCIO (QUERY PARAMS) ---

    @Operation(summary = "Filtrar planes por rango de precio", description = "Busca planes ideales según el presupuesto del cliente")
    @GetMapping("/filter-price")
    public ResponseEntity<List<SubscriptionPlanResponseDto>> getByPriceRange(
            @Parameter(description = "Precio mínimo del plan", example = "10.00")
            @RequestParam BigDecimal min,
            @Parameter(description = "Precio máximo del plan", example = "50.00")
            @RequestParam BigDecimal max) {
        return ResponseEntity.ok(planService.findPlansByPriceRange(min, max));
    }

    @Operation(summary = "Filtrar por nivel de soporte", description = "Busca planes cruzando con la tabla DataCatalogo")
    @GetMapping("/filter-support")
    public ResponseEntity<List<SubscriptionPlanResponseDto>> getBySupport(
            @Parameter(description = "Descripción del nivel (Debe existir en DataCatalogo)", example = "Premium")
            @RequestParam String supportLevel) {
        return ResponseEntity.ok(planService.findBySupportLevelName(supportLevel));
    }

    @Operation(summary = "Análisis de usuarios por plan", description = "Métrica SaaS: Cuántos usuarios activos tiene este plan")
    @GetMapping("/{id}/active-users")
    public ResponseEntity<Long> getActiveUserCount(
            @Parameter(description = "ID del plan a analizar", example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(planService.countActiveUsersByPlan(id));
    }
}