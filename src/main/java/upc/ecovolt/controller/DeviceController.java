package upc.ecovolt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import upc.ecovolt.mapping.dto.ApiResponseDto;
import upc.ecovolt.mapping.dto.DeviceDto;
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

    // --- ACCIONES CON NOTIFICACIÓN (POST, PUT, DELETE) ---

    @Operation(summary = "Vincular nuevo dispositivo",
            description = "RESTRICTED: Solo el dueño de la casa. Registra un equipo y valida límites del plan SaaS.")
    @ApiResponse(responseCode = "201", description = "Dispositivo vinculado correctamente")
    @ApiResponse(responseCode = "400", description = "Límite de plan excedido o serial duplicado")
    @PostMapping
    public ResponseEntity<ApiResponseDto<DeviceDto.Response>> create(@Valid @RequestBody DeviceDto.Request request) {
        var data = deviceService.saveDevice(request);
        return new ResponseEntity<>(ApiResponseDto.<DeviceDto.Response>builder()
                .title("¡Registro Exitoso!")
                .message("El equipo '" + data.getName() + "' ha sido vinculado a tu red de ahorro.")
                .status("SUCCESS")
                .data(data)
                .build(), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar configuración del equipo",
            description = "RESTRICTED: Solo el dueño. Permite actualizar nombre y firmware.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<DeviceDto.Response>> update(
            @Parameter(description = "ID del dispositivo", example = "1") @PathVariable Long id,
            @Valid @RequestBody DeviceDto.Request request) {

        var data = deviceService.updateDevice(id, request);
        return ResponseEntity.ok(ApiResponseDto.<DeviceDto.Response>builder()
                .title("Actualización Completa")
                .message("La configuración de " + data.getName() + " se guardó con éxito.")
                .status("SUCCESS")
                .data(data)
                .build());
    }

    @Operation(summary = "Eliminar un dispositivo", description = "RESTRICTED: Solo el dueño. Acción irreversible.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> delete(
            @Parameter(description = "ID del dispositivo a eliminar", example = "2") @PathVariable Long id) {

        deviceService.delete(id);
        return ResponseEntity.ok(ApiResponseDto.<Void>builder()
                .title("Dispositivo Removido")
                .message("El equipo ha sido desconectado del sistema permanentemente.")
                .status("SUCCESS")
                .build());
    }

    // --- CONSULTAS Y ANALÍTICA (DATA DIRECTA) ---

    @Operation(summary = "Listar todos los dispositivos", description = "ADMIN/SUPPORT ONLY: Auditoría global de hardware.")
    @GetMapping
    public ResponseEntity<List<DeviceDto.Response>> getAll() {
        return ResponseEntity.ok(deviceService.findAllDevices());
    }

    @Operation(summary = "Obtener un dispositivo por ID", description = "ACCESO POR PROPIEDAD: Detalles técnicos del sensor.")
    @GetMapping("/{id}")
    public ResponseEntity<DeviceDto.Response> getById(@PathVariable Long id) {
        return deviceService.findDeviceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar por Número de Serie", description = "Utilizado para vinculación rápida vía QR o UUID.")
    @GetMapping("/serial/{serialNumber}")
    public ResponseEntity<DeviceDto.Response> getBySerial(
            @Parameter(description = "Número de serie único", example = "SN-LIG-001") @PathVariable String serialNumber) {
        return deviceService.findBySerialNumber(serialNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar dispositivos de una vivienda", description = "Muestra todo el ecosistema IoT de una propiedad.")
    @GetMapping("/home/{homeId}")
    public ResponseEntity<List<DeviceDto.Response>> getByHome(@PathVariable Long homeId) {
        return ResponseEntity.ok(deviceService.findByHomeId(homeId));
    }

    @Operation(summary = "Auditoría de salud por usuario",
            description = "SUPPORT ONLY: Cuenta equipos en estado de falla o activos para soporte técnico.")
    @GetMapping("/status-count")
    public ResponseEntity<Long> getStatusCount(
            @Parameter(description = "ID del usuario a auditar", example = "6") @RequestParam Long userId,
            @Parameter(description = "1: Activo, 0: Inactivo, 2: Falla", example = "1") @RequestParam Integer status) {
        return ResponseEntity.ok(deviceService.countByUserIdAndStatus(userId, status));
    }
}
