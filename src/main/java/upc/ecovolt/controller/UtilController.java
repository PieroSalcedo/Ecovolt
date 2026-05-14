package upc.ecovolt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upc.ecovolt.mapping.dto.datacatalogodto.DataCatalogoResponseDto;
import upc.ecovolt.service.DataCatalogoService;
import upc.ecovolt.util.AppSettings;

import java.util.List;

@Tag(name = "Utilities", description = "Endpoints para cargar diccionarios y catálogos (Combos)")
@RestController
@RequestMapping("/api/v1/util")
@RequiredArgsConstructor
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class UtilController {

    private final DataCatalogoService dataCatalogoService;

    @Operation(summary = "Lista los tipos de habitaciones disponibles")
    @GetMapping("/room-types")
    public ResponseEntity<List<DataCatalogoResponseDto>> listRoomTypes() {
        // Usamos la constante que definimos en AppSettings
        return ResponseEntity.ok(dataCatalogoService.findByCatalogDescription("ROOM_TYPES"));
    }

    @Operation(summary = "Lista las categorías de dispositivos IoT")
    @GetMapping("/device-categories")
    public ResponseEntity<List<DataCatalogoResponseDto>> listDeviceCategories() {
        return ResponseEntity.ok(dataCatalogoService.findByCatalogDescription("DEVICE_CATEGORIES"));
    }

    @Operation(summary = "Lista los niveles de soporte para los planes")
    @GetMapping("/support-levels")
    public ResponseEntity<List<DataCatalogoResponseDto>> listSupportLevels() {
        return ResponseEntity.ok(dataCatalogoService.findByCatalogDescription("SUPPORT_LEVELS"));
    }

    @Operation(summary = "Lista los estados operativos de los equipos")
    @GetMapping("/device-status")
    public ResponseEntity<List<DataCatalogoResponseDto>> listDeviceStatus() {
        return ResponseEntity.ok(dataCatalogoService.findByCatalogDescription("DEVICE_STATUS"));
    }
}
