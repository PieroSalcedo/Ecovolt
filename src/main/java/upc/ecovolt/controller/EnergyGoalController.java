package upc.ecovolt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upc.ecovolt.mapping.dto.ApiResponseDto;
import upc.ecovolt.mapping.dto.EnergyGoalDto;
import upc.ecovolt.service.EnergyGoalService;
import upc.ecovolt.util.WebUtil;

import java.util.List;

@Tag(name = "Energy Goals", description = "Controlador para gestión y monitoreo de metas de ahorro energético")
@RestController
@RequestMapping("/api/v1/energy-goals")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EnergyGoalController {

    private final EnergyGoalService goalService;

    @PostMapping
    @Operation(summary = "Crear una nueva meta de ahorro para una vivienda")
    public ResponseEntity<ApiResponseDto<EnergyGoalDto.Response>> registrar(@RequestBody EnergyGoalDto.Request request) {
        var respuesta = goalService.save(request);
        return WebUtil.ok(respuesta, "Meta de ahorro establecida correctamente.");
    }

    @GetMapping("/home/{idHome}/active")
    @Operation(summary = "Obtener metas activas asociadas a una vivienda específica")
    public ResponseEntity<ApiResponseDto<List<EnergyGoalDto.Response>>> obtenerMetasActivasPorVivienda(@PathVariable("idHome") Long idHome) {
        var lista = goalService.findActiveGoalsByHome(idHome);
        return WebUtil.ok(lista, "Metas activas cargadas correctamente.");
    }

    @GetMapping("/critical")
    public ResponseEntity<ApiResponseDto<List<EnergyGoalDto.Response>>> obtenerMetasCriticas(@RequestParam(name = "threshold", defaultValue = "80") Integer threshold) {
        var lista = goalService.findCriticalGoals(threshold);
        return WebUtil.ok(lista, "Reporte de metas críticas generado.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> eliminar(@PathVariable("id") Integer id) {
        goalService.delete(id);
        return WebUtil.ok(null, "Meta de ahorro desactivada correctamente.");
    }

    @GetMapping("/active-by-type")
    public ResponseEntity<ApiResponseDto<EnergyGoalDto.Response>> obtenerMetaActiva(
            @RequestParam(name = "type") String type,
            @RequestParam(name = "id") Long id) {

        // type vendrá como 'CASA', 'CUARTO' o 'DISPOSITIVO'
        var meta = goalService.findActiveByTypeAndId(type, id);
        return WebUtil.ok(meta, "Meta recuperada");
    }
}