package upc.ecovolt.controller;

import upc.ecovolt.mapping.dto.subscriptionplandto.SubscriptionPlanRequestDto;
import upc.ecovolt.mapping.dto.subscriptionplandto.SubscriptionPlanResponseDto;
import upc.ecovolt.service.SubscriptionPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Subscription Plans", description = "Endpoints para la gestión de planes SaaS de EcoVolt")
@RestController
@RequestMapping("/api/v1/plans")
@RequiredArgsConstructor
public class SubscriptionPlanController {

    private final SubscriptionPlanService planService;

    @Operation(summary = "Obtener todos los planes de suscripción")
    @GetMapping
    public ResponseEntity<List<SubscriptionPlanResponseDto>> getAllPlans() {
        return ResponseEntity.ok(planService.findAllPlans());
    }

    @Operation(summary = "Obtener un plan por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionPlanResponseDto> getPlanById(@PathVariable Long id) {
        return planService.findPlanById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo plan de suscripción")
    @PostMapping
    public ResponseEntity<SubscriptionPlanResponseDto> createPlan(@Valid @RequestBody SubscriptionPlanRequestDto request) {
        var response = planService.savePlan(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED); // 201 Created
    }

    @Operation(summary = "Actualizar un plan existente")
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionPlanResponseDto> updatePlan(@PathVariable Long id, @Valid @RequestBody SubscriptionPlanRequestDto request) {
        return ResponseEntity.ok(planService.updatePlan(id, request));
    }

    @Operation(summary = "Eliminar un plan (Desactivación lógica)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable Long id) {
        planService.delete(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}