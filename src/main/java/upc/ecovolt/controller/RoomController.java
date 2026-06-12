package upc.ecovolt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upc.ecovolt.mapping.dto.ApiResponseDto;
import upc.ecovolt.mapping.dto.RoomDto;
import upc.ecovolt.service.RoomService;
import upc.ecovolt.util.WebUtil;
import upc.ecovolt.util.AppSettings;

import java.util.List;

@Tag(name = "Rooms", description = "Gestión de ambientes y distribución por casa")
@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN) // Habilita la conexión con Angular
public class RoomController {

    private final RoomService roomService;

    // --- REGISTRO ---
    @PostMapping
    @Operation(summary = "Registrar habitación", description = "Crea un ambiente vinculado a una vivienda (Ej: Cocina, Dormitorio)")
    public ResponseEntity<ApiResponseDto<RoomDto.Response>> create(@RequestBody RoomDto.Request request) {
        var data = roomService.saveRoom(request);
        return WebUtil.created(data, "El ambiente '" + data.getName() + "' ha sido registrado.");
    }

    // --- ACTUALIZACIÓN ---
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar ambiente")
    public ResponseEntity<ApiResponseDto<RoomDto.Response>> update(@PathVariable Long id, @RequestBody RoomDto.Request request) {
        var data = roomService.updateRoom(id, request);
        return WebUtil.ok(data, "Habitación actualizada correctamente.");
    }

    // --- ELIMINACIÓN ---
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar ambiente", description = "Realiza un borrado lógico (status=0)")
    public ResponseEntity<ApiResponseDto<Void>> delete(@PathVariable Long id) {
        roomService.delete(id);
        return WebUtil.ok(null, "Ambiente removido del sistema.");
    }

    // --- LISTADOS PARA EL FRONTEND (Cascading Selects) ---

    @GetMapping("/home/{homeId}")
    @Operation(summary = "Listar ambientes por casa",
            description = "ESTE LLENA EL COMBOBOX de habitaciones en el formulario de dispositivos.")
    public ResponseEntity<ApiResponseDto<List<RoomDto.Response>>> getByHome(@PathVariable Long homeId) {
        var data = roomService.findByHomeId(homeId);
        return WebUtil.ok(data, "Habitaciones de la propiedad cargadas.");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle de habitación")
    public ResponseEntity<ApiResponseDto<RoomDto.Response>> getById(@PathVariable Long id) {
        return roomService.findRoomById(id)
                .map(room -> WebUtil.ok(room, "Detalle del ambiente"))
                .orElseThrow(() -> new RuntimeException("No se encontró la habitación"));
    }

    @GetMapping("/{id}/devices/count")
    @Operation(summary = "Contar dispositivos", description = "Muestra cuántos equipos IoT hay en este cuarto específico.")
    public ResponseEntity<ApiResponseDto<Long>> getDeviceCount(@PathVariable Long id) {
        var count = roomService.countDevicesInRoom(id);
        return WebUtil.ok(count, "Conteo de dispositivos en el ambiente.");
    }
}