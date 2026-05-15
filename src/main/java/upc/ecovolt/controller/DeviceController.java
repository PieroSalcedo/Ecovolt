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
import upc.ecovolt.mapping.dto.devicedto.DeviceRequestDto;
import upc.ecovolt.mapping.dto.devicedto.DeviceResponseDto;
import upc.ecovolt.service.DeviceService;
import upc.ecovolt.util.AppSettings;

import java.util.List;

@Tag(name = "Devices", description = "Endpoints para la vinculación de hardware IoT y gestión de límites SaaS")
@RestController
@RequestMapping("/api/v1/devices")
@RequiredArgsConstructor
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class DeviceController {

    private final DeviceService deviceService;

    // --- ACCIONES QUE GENERAN NOTIFICACIONES (POST, PUT, DELETE) ---

    @Operation(summary = "Vincular nuevo dispositivo", description = "Registra un equipo y valida límites del plan.")
    @PostMapping
    public ResponseEntity<ApiResponseDto<DeviceResponseDto>> create(@Valid @RequestBody DeviceRequestDto request) {
        var data = deviceService.saveDevice(request);

        return new ResponseEntity<>(ApiResponseDto.<DeviceResponseDto>builder()
                .title("¡Registro Exitoso!")
                .message("El dispositivo '" + data.getName() + "' ha sido vinculado a la red.")
                .status("SUCCESS")
                .data(data)
                .build(), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar dispositivo")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<DeviceResponseDto>> update(
            @Parameter(description = "ID del dispositivo", example = "1") @PathVariable Long id,
            @Valid @RequestBody DeviceRequestDto request) {

        var data = deviceService.updateDevice(id, request);

        return ResponseEntity.ok(ApiResponseDto.<DeviceResponseDto>builder()
                .title("Configuración Actualizada")
                .message("Los cambios en el equipo " + data.getName() + " se guardaron correctamente.")
                .status("SUCCESS")
                .data(data)
                .build());
    }

    @Operation(summary = "Eliminar un dispositivo")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> delete(
            @Parameter(description = "ID del dispositivo", example = "2") @PathVariable Long id) {

        deviceService.delete(id);

        return ResponseEntity.ok(ApiResponseDto.<Void>builder()
                .title("Dispositivo Removido")
                .message("El equipo ha sido desconectado y eliminado del inventario.")
                .status("SUCCESS")
                .build());
    }

    // --- MÉTODOS DE CONSULTA (DATA DIRECTA) ---

    @Operation(summary = "Listar todos los dispositivos")
    @GetMapping
    public ResponseEntity<List<DeviceResponseDto>> getAll() {
        return ResponseEntity.ok(deviceService.findAllDevices());
    }

    @Operation(summary = "Obtener un dispositivo por ID")
    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponseDto> getById(@PathVariable Long id) {
        return deviceService.findDeviceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar por Número de Serie")
    @GetMapping("/serial/{serialNumber}")
    public ResponseEntity<DeviceResponseDto> getBySerial(@PathVariable String serialNumber) {
        return deviceService.findBySerialNumber(serialNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar dispositivos de una vivienda")
    @GetMapping("/home/{homeId}")
    public ResponseEntity<List<DeviceResponseDto>> getByHome(@PathVariable Long homeId) {
        return ResponseEntity.ok(deviceService.findByHomeId(homeId));
    }

    @Operation(summary = "Estado de salud de dispositivos por usuario")
    @GetMapping("/status-count")
    public ResponseEntity<Long> getStatusCount(
            @RequestParam Long userId,
            @RequestParam Integer status) {
        return ResponseEntity.ok(deviceService.countByUserIdAndStatus(userId, status));
    }
}