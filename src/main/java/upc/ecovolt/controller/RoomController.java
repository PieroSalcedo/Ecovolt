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
import upc.ecovolt.mapping.dto.roomdto.RoomRequestDto;
import upc.ecovolt.mapping.dto.roomdto.RoomResponseDto;
import upc.ecovolt.service.RoomService;
import upc.ecovolt.util.AppSettings;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Rooms", description = "Endpoints para la gestión de ambientes, distribución por pisos y auditoría de carga IoT")
@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class RoomController {

    private final RoomService roomService;

    @Operation(summary = "Listar todos los ambientes", description = "Retorna todos los cuartos registrados en el sistema")
    @GetMapping
    public ResponseEntity<List<RoomResponseDto>> getAll() {
        return ResponseEntity.ok(roomService.findAllRooms());
    }

    @Operation(summary = "Obtener un ambiente por ID")
    @ApiResponse(responseCode = "200", description = "Ambiente encontrado")
    @ApiResponse(responseCode = "404", description = "Ambiente no existe")
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponseDto> getById(
            @Parameter(description = "ID único del ambiente", example = "1")
            @PathVariable Long id) {
        return roomService.findRoomById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Registrar un nuevo ambiente", description = "Crea un cuarto vinculado a una casa y define su tipo (Cocina, Sala, etc.)")
    @PostMapping
    public ResponseEntity<RoomResponseDto> create(@Valid @RequestBody RoomRequestDto request) {
        return new ResponseEntity<>(roomService.saveRoom(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar datos del ambiente", description = "Modifica el nombre, área o tipo de habitación")
    @PutMapping("/{id}")
    public ResponseEntity<RoomResponseDto> update(
            @Parameter(description = "ID del ambiente a modificar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody RoomRequestDto request) {
        return ResponseEntity.ok(roomService.updateRoom(id, request));
    }

    @Operation(summary = "Eliminar un ambiente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del ambiente a eliminar", example = "2")
            @PathVariable Long id) {
        roomService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --- ENDPOINTS DE INTELIGENCIA Y JERARQUÍA ---

    @Operation(summary = "Listar ambientes de una casa", description = "Obtiene todas las habitaciones que pertenecen a una vivienda específica")
    @GetMapping("/home/{homeId}")
    public ResponseEntity<List<RoomResponseDto>> getByHome(
            @Parameter(description = "ID de la vivienda", example = "1")
            @PathVariable Long homeId) {
        return ResponseEntity.ok(roomService.findByHomeId(homeId));
    }

    @Operation(summary = "Filtrar por tipo de ambiente", description = "Busca habitaciones según su categoría en DataCatalogo (Ej: 'Cocina')")
    @GetMapping("/type")
    public ResponseEntity<List<RoomResponseDto>> getByType(
            @Parameter(description = "Descripción del tipo de cuarto", example = "Kitchen")
            @RequestParam String typeName) {
        return ResponseEntity.ok(roomService.findByRoomTypeName(typeName));
    }

    @Operation(summary = "Contar dispositivos en el ambiente", description = "Auditoría de inventario: ¿Cuántos sensores hay en este cuarto?")
    @GetMapping("/{id}/devices/count")
    public ResponseEntity<Long> getDeviceCountInRoom(
            @Parameter(description = "ID del ambiente", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(roomService.countDevicesInRoom(id));
    }

    @Operation(summary = "Filtrar por piso y casa", description = "Permite navegar la estructura vertical de la propiedad")
    @GetMapping("/home/{homeId}/floor/{floor}")
    public ResponseEntity<List<RoomResponseDto>> getByFloor(
            @Parameter(description = "ID de la vivienda", example = "1") @PathVariable Long homeId,
            @Parameter(description = "Número de piso", example = "2") @PathVariable Integer floor) {
        return ResponseEntity.ok(roomService.findByHomeAndFloor(homeId, floor));
    }

    @Operation(summary = "Buscar ambientes grandes", description = "Identifica habitaciones que superan un área en m2 (Para optimización de clima)")
    @GetMapping("/large-rooms")
    public ResponseEntity<List<RoomResponseDto>> getLargeRooms(
            @Parameter(description = "Área mínima en m2", example = "25.0")
            @RequestParam BigDecimal minArea) {
        return ResponseEntity.ok(roomService.findLargeRooms(minArea));
    }
}