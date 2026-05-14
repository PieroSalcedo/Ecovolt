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
import upc.ecovolt.mapping.dto.homedto.HomeRequestDto;
import upc.ecovolt.mapping.dto.homedto.HomeResponseDto;
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

    @Operation(summary = "Listar todas las viviendas registradas", description = "Retorna el historial completo de propiedades en el sistema")
    @GetMapping
    public ResponseEntity<List<HomeResponseDto>> getAll() {
        return ResponseEntity.ok(homeService.findAllHomes());
    }

    @Operation(summary = "Obtener una vivienda por su ID", description = "Busca la información técnica y tarifaria de una propiedad específica")
    @ApiResponse(responseCode = "200", description = "Vivienda encontrada")
    @ApiResponse(responseCode = "404", description = "Vivienda no existe")
    @GetMapping("/{id}")
    public ResponseEntity<HomeResponseDto> getById(
            @Parameter(description = "ID único de la vivienda", example = "1")
            @PathVariable Long id) {
        return homeService.findHomeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Registrar una nueva propiedad", description = "Crea una vivienda vinculándola a un usuario y asignando un tipo (Casa, Dpto, etc.)")
    @PostMapping
    public ResponseEntity<HomeResponseDto> create(@Valid @RequestBody HomeRequestDto request) {
        return new ResponseEntity<>(homeService.saveHome(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar datos de la vivienda", description = "Modifica la dirección, alias o la tarifa eléctrica (kWh)")
    @PutMapping("/{id}")
    public ResponseEntity<HomeResponseDto> update(
            @Parameter(description = "ID de la vivienda a modificar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody HomeRequestDto request) {
        return ResponseEntity.ok(homeService.updateHome(id, request));
    }

    @Operation(summary = "Eliminar una vivienda", description = "Desactiva la propiedad del ecosistema Ecovolt")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la vivienda a eliminar", example = "10")
            @PathVariable Long id) {
        homeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --- ENDPOINTS DE INTELIGENCIA DE NEGOCIO ---

    @Operation(summary = "Listar viviendas de un usuario", description = "Filtra todas las propiedades que pertenecen a un cliente específico")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<HomeResponseDto>> getByUser(
            @Parameter(description = "ID del usuario dueño", example = "1")
            @PathVariable Long userId) {
        return ResponseEntity.ok(homeService.findActiveHomesByUser(userId));
    }

    @Operation(summary = "Auditoría de hardware por casa", description = "Retorna el conteo total de dispositivos IoT instalados en todas las habitaciones de la casa")
    @GetMapping("/{id}/device-count")
    public ResponseEntity<Long> getDeviceCount(
            @Parameter(description = "ID de la vivienda para el conteo", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(homeService.countTotalDevicesByHome(id));
    }

    @Operation(summary = "Filtrar por tarifa elevada", description = "Busca hogares cuya tarifa de energía supere un valor umbral para campañas de ahorro")
    @GetMapping("/high-tariff")
    public ResponseEntity<List<HomeResponseDto>> getByHighTariff(
            @Parameter(description = "Precio kWh mínimo a filtrar", example = "0.65")
            @RequestParam BigDecimal threshold) {
        return ResponseEntity.ok(homeService.findHomesByHighTariff(threshold));
    }

    @Operation(summary = "Búsqueda por Alias y Usuario", description = "Permite al usuario seleccionar su casa mediante un nombre amigable (Ej: 'Oficina')")
    @GetMapping("/search")
    public ResponseEntity<List<HomeResponseDto>> getByAlias(
            @Parameter(description = "Alias de la propiedad", example = "Casa Playa") @RequestParam String alias,
            @Parameter(description = "ID del usuario", example = "1") @RequestParam Long userId) {
        return ResponseEntity.ok(homeService.findByAliasAndUserId(alias, userId));
    }
}