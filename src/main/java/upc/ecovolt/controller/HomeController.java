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
import upc.ecovolt.mapping.dto.ApiResponseDto;
import upc.ecovolt.mapping.dto.HomeDto;
import upc.ecovolt.service.HomeService;
import upc.ecovolt.util.AppSettings;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Homes", description = "Endpoints para la gestión de viviendas, tarifas eléctricas y auditoría de inventario")
@RestController
@RequestMapping("/api/v1/homes")
@RequiredArgsConstructor
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class HomeController {

    private final HomeService homeService;

    // --- ACCIONES CON NOTIFICACIÓN (POST, PUT, DELETE) ---

    @Operation(summary = "Registrar una nueva propiedad", description = "Crea una vivienda vinculándola al usuario autenticado.")
    @ApiResponse(responseCode = "201", description = "Vivienda registrada exitosamente")
    @PostMapping
    public ResponseEntity<ApiResponseDto<HomeDto.Response>> create(@Valid @RequestBody HomeDto.Request request) {
        var data = homeService.saveHome(request);

        return new ResponseEntity<>(ApiResponseDto.<HomeDto.Response>builder()
                .title("¡Vivienda Registrada!")
                .message("La propiedad '" + data.getAlias() + "' ha sido vinculada correctamente a su cuenta.")
                .status("SUCCESS")
                .data(data)
                .build(), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar datos de la vivienda", description = "Permite modificar la dirección, el alias o la tarifa de consumo (kWh).")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<HomeDto.Response>> update(
            @Parameter(description = "ID de la vivienda a modificar", example = "1") @PathVariable Long id,
            @Valid @RequestBody HomeDto.Request request) {

        var data = homeService.updateHome(id, request);

        return ResponseEntity.ok(ApiResponseDto.<HomeDto.Response>builder()
                .title("Información Actualizada")
                .message("Los cambios en '" + data.getAlias() + "' se guardaron con éxito.")
                .status("SUCCESS")
                .data(data)
                .build());
    }

    @Operation(summary = "Eliminar una vivienda", description = "Retira la propiedad del sistema. Esta acción es irreversible.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> delete(
            @Parameter(description = "ID de la vivienda a eliminar", example = "1") @PathVariable Long id) {

        homeService.delete(id);

        return ResponseEntity.ok(ApiResponseDto.<Void>builder()
                .title("Propiedad Eliminada")
                .message("La vivienda y sus configuraciones asociadas han sido removidas del sistema.")
                .status("SUCCESS")
                .build());
    }

    // --- CONSULTAS DE DATOS (DATA DIRECTA) ---

    @Operation(summary = "Listar todas las viviendas (Staff)", description = "ACCESO RESTRINGIDO: Solo Admin, Auditor y Analista.")
    @GetMapping
    public ResponseEntity<List<HomeDto.Response>> getAll() {
        return ResponseEntity.ok(homeService.findAllHomes());
    }

    @Operation(summary = "Obtener vivienda por ID", description = "Busca detalles técnicos de una propiedad.")
    @GetMapping("/{id}")
    public ResponseEntity<HomeDto.Response> getById(@PathVariable Long id) {
        return homeService.findHomeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar viviendas por Usuario", description = "Obtiene todas las casas que pertenecen a un cliente específico.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<HomeDto.Response>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(homeService.findActiveHomesByUser(userId));
    }

    @Operation(summary = "Auditoría de hardware por casa", description = "Retorna el conteo total de dispositivos instalados en toda la casa.")
    @GetMapping("/{id}/device-count")
    public ResponseEntity<Long> getDeviceCount(@PathVariable Long id) {
        return ResponseEntity.ok(homeService.countTotalDevicesByHome(id));
    }

    @Operation(summary = "Filtrar por tarifa elevada", description = "Analítica: Busca hogares con costos de energía críticos.")
    @GetMapping("/high-tariff")
    public ResponseEntity<List<HomeDto.Response>> getByHighTariff(@RequestParam BigDecimal threshold) {
        return ResponseEntity.ok(homeService.findHomesByHighTariff(threshold));
    }

    @Operation(summary = "Búsqueda por Alias", description = "Busca una propiedad por su nombre amigable.")
    @GetMapping("/search")
    public ResponseEntity<List<HomeDto.Response>> getByAlias(
            @RequestParam String alias, @RequestParam Long userId) {
        return ResponseEntity.ok(homeService.findByAliasAndUserId(alias, userId));
    }
}
