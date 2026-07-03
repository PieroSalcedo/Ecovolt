package upc.ecovolt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upc.ecovolt.mapping.dto.ApiResponseDto;
import upc.ecovolt.mapping.dto.DeviceDto;
import upc.ecovolt.service.DeviceService;
import upc.ecovolt.util.WebUtil;
import upc.ecovolt.util.AppSettings;

import java.util.List;

@Tag(name = "Devices", description = "Gestión de hardware IoT e inventario")
@RestController
@RequestMapping("/api/v1/devices")
@RequiredArgsConstructor
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN) // Para conectar con Angular
public class DeviceController {

    private final DeviceService deviceService;

    @GetMapping("/consultaDinamica")
    @Operation(summary = "Consulta dinámica de dispositivos")
    public ResponseEntity<ApiResponseDto<List<DeviceDto.Response>>> consulta(
            @RequestParam(name = "idHome", defaultValue = "-1") Long idHome,
            @RequestParam(name = "idRoom", defaultValue = "-1") Long idRoom,
            @RequestParam(name = "name", defaultValue = "") String name) {

        var lista = deviceService.consultaDispositivoDinamica(idHome, idRoom, name);
        return WebUtil.ok(lista, "Dispositivos cargados con éxito");
    }

    // --- REGISTRO ---
    @PostMapping
    @Operation(summary = "Vincular dispositivo", description = "Valida límites de plan y propiedad del cuarto")
    public ResponseEntity<ApiResponseDto<DeviceDto.Response>> create(@RequestBody DeviceDto.Request request) {
        var data = deviceService.saveDevice(request);
        return WebUtil.created(data, "El dispositivo '" + data.getName() + "' ha sido vinculado.");
    }

    // --- ACTUALIZACIÓN ---
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar dispositivo", description = "Permite cambiar nombre o habitación")
    public ResponseEntity<ApiResponseDto<DeviceDto.Response>> update(@PathVariable Long id, @RequestBody DeviceDto.Request request) {
        var data = deviceService.updateDevice(id, request);
        return WebUtil.ok(data, "Configuración actualizada correctamente.");
    }

    // --- ELIMINACIÓN ---
    @DeleteMapping("/{id}")
    @Operation(summary = "Desvincular dispositivo", description = "Realiza un borrado lógico (status=0)")
    public ResponseEntity<ApiResponseDto<Void>> delete(@PathVariable Long id) {
        deviceService.delete(id);
        return WebUtil.ok(null, "El dispositivo ha sido retirado del sistema.");
    }

    // --- LISTADOS PARA TABLAS DEL FRONTEND ---

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<DeviceDto.Response>> getById(@PathVariable Long id) {
        return deviceService.findDeviceById(id)
                .map(device -> WebUtil.ok(device, "Detalle del dispositivo"))
                .orElseThrow(() -> new RuntimeException("No se encontró el dispositivo"));
    }

    @GetMapping("/home/{homeId}")
    @Operation(summary = "Listar dispositivos de una casa", description = "Usado para el Dashboard de la vivienda")
    public ResponseEntity<ApiResponseDto<List<DeviceDto.Response>>> getByHome(@PathVariable Long homeId) {
        var data = deviceService.findByHomeId(homeId);
        return WebUtil.ok(data, "Lista de dispositivos cargada.");
    }

    @GetMapping("/room/{roomId}")
    @Operation(summary = "Listar dispositivos de una habitación")
    public ResponseEntity<ApiResponseDto<List<DeviceDto.Response>>> getByRoom(@PathVariable Long roomId) {
        var data = deviceService.findByRoomId(roomId);
        return WebUtil.ok(data, "Dispositivos en el ambiente cargados.");
    }
}