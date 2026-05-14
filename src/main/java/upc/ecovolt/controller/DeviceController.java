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
import upc.ecovolt.mapping.dto.devicedto.DeviceRequestDto;
import upc.ecovolt.mapping.dto.devicedto.DeviceResponseDto;
import upc.ecovolt.service.DeviceService;
import upc.ecovolt.util.AppSettings;

import java.util.List;

@Tag(name = "Devices", description = "Endpoints para la vinculación de hardware IoT, gestión de categorías y validación de límites de suscripción")
@RestController
@RequestMapping("/api/v1/devices")
@RequiredArgsConstructor
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class DeviceController {

    private final DeviceService deviceService;

    @Operation(summary = "Listar todos los dispositivos", description = "Retorna el inventario global de equipos registrados")
    @GetMapping
    public ResponseEntity<List<DeviceResponseDto>> getAll() {
        return ResponseEntity.ok(deviceService.findAllDevices());
    }

    @Operation(summary = "Obtener un dispositivo por ID")
    @ApiResponse(responseCode = "200", description = "Dispositivo encontrado")
    @ApiResponse(responseCode = "404", description = "Dispositivo no existe")
    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponseDto> getById(
            @Parameter(description = "ID único del dispositivo", example = "1")
            @PathVariable Long id) {
        return deviceService.findDeviceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Vincular nuevo dispositivo (IoT Gateway)",
            description = "Registra un equipo físico. IMPORTANTE: Este endpoint valida si el usuario ha superado el límite de dispositivos de su plan SaaS.")
    @ApiResponse(responseCode = "201", description = "Vinculación exitosa")
    @ApiResponse(responseCode = "400", description = "Límite de plan superado o Serial duplicado")
    @PostMapping
    public ResponseEntity<DeviceResponseDto> create(@Valid @RequestBody DeviceRequestDto request) {
        return new ResponseEntity<>(deviceService.saveDevice(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar información del dispositivo", description = "Modifica el nombre o firmware. El número de serie no es editable.")
    @PutMapping("/{id}")
    public ResponseEntity<DeviceResponseDto> update(
            @Parameter(description = "ID del dispositivo", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody DeviceRequestDto request) {
        return ResponseEntity.ok(deviceService.updateDevice(id, request));
    }

    @Operation(summary = "Eliminar un dispositivo")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del dispositivo a eliminar", example = "2")
            @PathVariable Long id) {
        deviceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --- ENDPOINTS DE INTELIGENCIA Y HARDWARE ---

    @Operation(summary = "Buscar por Número de Serie", description = "Identifica un equipo por su identidad física única (MAC Address / UUID)")
    @GetMapping("/serial/{serialNumber}")
    public ResponseEntity<DeviceResponseDto> getBySerial(
            @Parameter(description = "Serial del hardware", example = "EV-2024-X99")
            @PathVariable String serialNumber) {
        return deviceService.findBySerialNumber(serialNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar por categoría", description = "Filtra equipos usando el DataCatalogo (Ej: 'Lighting', 'Appliances')")
    @GetMapping("/category")
    public ResponseEntity<List<DeviceResponseDto>> getByCategory(
            @Parameter(description = "Nombre de la categoría", example = "Lighting")
            @RequestParam String categoryName) {
        return ResponseEntity.ok(deviceService.findByCategoryName(categoryName));
    }

    @Operation(summary = "Listar dispositivos de una vivienda", description = "Navegación jerárquica: Muestra todos los equipos de una propiedad completa")
    @GetMapping("/home/{homeId}")
    public ResponseEntity<List<DeviceResponseDto>> getByHome(
            @Parameter(description = "ID de la vivienda", example = "1")
            @PathVariable Long homeId) {
        return ResponseEntity.ok(deviceService.findByHomeId(homeId));
    }

    @Operation(summary = "Estado de salud de dispositivos por usuario", description = "Cuenta cuántos equipos tiene un usuario en un estado específico (Ej: 1=Activo, 2=Falla)")
    @GetMapping("/status-count")
    public ResponseEntity<Long> getStatusCount(
            @Parameter(description = "ID del usuario", example = "1") @RequestParam Long userId,
            @Parameter(description = "Código de estado (1:Activo, 2:Falla)", example = "1") @RequestParam Integer status) {
        return ResponseEntity.ok(deviceService.countByUserIdAndStatus(userId, status));
    }

    @Operation(summary = "Listar dispositivos de una marca", description = "Análisis de mercado: Filtra equipos por fabricante")
    @GetMapping("/manufacturer")
    public ResponseEntity<List<DeviceResponseDto>> getByManufacturer(
            @Parameter(description = "Nombre del fabricante", example = "EcoVolt Hardware Corp")
            @RequestParam String name) {
        return ResponseEntity.ok(deviceService.findByManufacturer(name));
    }
}