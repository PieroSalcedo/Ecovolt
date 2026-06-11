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
import upc.ecovolt.mapping.dto.RoomDto;
import upc.ecovolt.service.RoomService;
import upc.ecovolt.util.AppSettings;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Rooms", description = "Endpoints para la gestión de ambientes y distribución por pisos")
@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class RoomController {

    private final RoomService roomService;

    // --- ACCIONES CON NOTIFICACIÓN (POST, PUT, DELETE) ---

    @Operation(summary = "Registrar un nuevo ambiente", description = "Crea un cuarto vinculado a una casa (Ej: Cocina, Sala).")
    @ApiResponse(responseCode = "201", description = "Ambiente creado exitosamente")
    @PostMapping
    public ResponseEntity<ApiResponseDto<RoomDto.Response>> create(@Valid @RequestBody RoomDto.Request request) {
        var data = roomService.saveRoom(request);

        return new ResponseEntity<>(ApiResponseDto.<RoomDto.Response>builder()
                .title("¡Ambiente Registrado!")
                .message("El cuarto '" + data.getName() + "' ha sido añadido correctamente a su vivienda.")
                .status("SUCCESS")
                .data(data)
                .build(), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar datos del ambiente", description = "Modifica el nombre, área o tipo de habitación.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<RoomDto.Response>> update(
            @Parameter(description = "ID del ambiente a modificar", example = "1") @PathVariable Long id,
            @Valid @RequestBody RoomDto.Request request) {

        var data = roomService.updateRoom(id, request);

        return ResponseEntity.ok(ApiResponseDto.<RoomDto.Response>builder()
                .title("Ambiente Actualizado")
                .message("Los cambios en '" + data.getName() + "' se guardaron con éxito.")
                .status("SUCCESS")
                .data(data)
                .build());
    }

    @Operation(summary = "Eliminar un ambiente", description = "Elimina la habitación y sus configuraciones. Es una acción irreversible.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> delete(
            @Parameter(description = "ID del ambiente a eliminar", example = "2") @PathVariable Long id) {

        roomService.delete(id);

        return ResponseEntity.ok(ApiResponseDto.<Void>builder()
                .title("Ambiente Eliminado")
                .message("La habitación ha sido removida del sistema de forma permanente.")
                .status("SUCCESS")
                .build());
    }

    // --- CONSULTAS DE DATOS (DATA DIRECTA) ---

    @Operation(summary = "Listar todos los ambientes", description = "Uso administrativo/Staff")
    @GetMapping
    public ResponseEntity<List<RoomDto.Response>> getAll() {
        return ResponseEntity.ok(roomService.findAllRooms());
    }

    @Operation(summary = "Obtener un ambiente por ID")
    @GetMapping("/{id}")
    public ResponseEntity<RoomDto.Response> getById(@PathVariable Long id) {
        return roomService.findRoomById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar ambientes de una casa")
    @GetMapping("/home/{homeId}")
    public ResponseEntity<List<RoomDto.Response>> getByHome(@PathVariable Long homeId) {
        return ResponseEntity.ok(roomService.findByHomeId(homeId));
    }

    @Operation(summary = "Filtrar por tipo de ambiente (DataCatalogo)")
    @GetMapping("/type")
    public ResponseEntity<List<RoomDto.Response>> getByType(@RequestParam String typeName) {
        return ResponseEntity.ok(roomService.findByRoomTypeName(typeName));
    }

    @Operation(summary = "Contar dispositivos en el ambiente")
    @GetMapping("/{id}/devices/count")
    public ResponseEntity<Long> getDeviceCountInRoom(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.countDevicesInRoom(id));
    }

    @Operation(summary = "Filtrar por piso y casa")
    @GetMapping("/home/{homeId}/floor/{floor}")
    public ResponseEntity<List<RoomDto.Response>> getByFloor(
            @PathVariable Long homeId, @PathVariable Integer floor) {
        return ResponseEntity.ok(roomService.findByHomeAndFloor(homeId, floor));
    }

    @Operation(summary = "Buscar ambientes grandes (Área m2)")
    @GetMapping("/large-rooms")
    public ResponseEntity<List<RoomDto.Response>> getLargeRooms(@RequestParam BigDecimal minArea) {
        return ResponseEntity.ok(roomService.findLargeRooms(minArea));
    }
}
