package upc.ecovolt.controller;

import upc.ecovolt.mapping.dto.devicedto.DeviceRequestDto;
import upc.ecovolt.mapping.dto.devicedto.DeviceResponseDto;
import upc.ecovolt.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Devices", description = "Gestión de sensores y dispositivos IoT de EcoVolt")
@RestController
@RequestMapping("/api/v1/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @Operation(summary = "Listar todos los dispositivos registrados")
    @GetMapping
    public ResponseEntity<List<DeviceResponseDto>> getAll() {
        return ResponseEntity.ok(deviceService.findAllDevices());
    }

    @Operation(summary = "Obtener un dispositivo por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponseDto> getById(@PathVariable Long id) {
        return deviceService.findDeviceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Vincular/Registrar un nuevo dispositivo")
    @PostMapping
    public ResponseEntity<DeviceResponseDto> create(@Valid @RequestBody DeviceRequestDto request) {
        return new ResponseEntity<>(deviceService.saveDevice(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar información de un dispositivo")
    @PutMapping("/{id}")
    public ResponseEntity<DeviceResponseDto> update(@PathVariable Long id, @Valid @RequestBody DeviceRequestDto request) {
        return ResponseEntity.ok(deviceService.updateDevice(id, request));
    }

    @Operation(summary = "Eliminar un dispositivo del sistema")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deviceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}