package upc.ecovolt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upc.ecovolt.mapping.dto.DataCatalogDto;
import upc.ecovolt.service.DataCatalogService;
import upc.ecovolt.util.AppSettings;

import java.util.List;

@Tag(name = "Utilities", description = "Endpoints para cargar diccionarios y catálogos (Combos)")
@RestController
@RequestMapping("/api/v1/util")
@RequiredArgsConstructor
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class UtilController {

    private final DataCatalogService dataCatalogService;

    @Operation(summary = "Lista los tipos de habitaciones disponibles")
    @GetMapping("/room-types")
    public ResponseEntity<List<DataCatalogDto.Response>> listRoomTypes() {
        // Usamos la constante que definimos en AppSettings
        return ResponseEntity.ok(dataCatalogService.findByCatalogDescription("ROOM_TYPES"));
    }

    @Operation(summary = "Lista las categorías de dispositivos IoT")
    @GetMapping("/device-categories")
    public ResponseEntity<List<DataCatalogDto.Response>> listDeviceCategories() {
        return ResponseEntity.ok(dataCatalogService.findByCatalogDescription("DEVICE_CATEGORIES"));
    }

    @Operation(summary = "Lista los niveles de soporte para los planes")
    @GetMapping("/support-levels")
    public ResponseEntity<List<DataCatalogDto.Response>> listSupportLevels() {
        return ResponseEntity.ok(dataCatalogService.findByCatalogDescription("SUPPORT_LEVELS"));
    }

    @Operation(summary = "Lista los estados operativos de los equipos")
    @GetMapping("/device-status")
    public ResponseEntity<List<DataCatalogDto.Response>> listDeviceStatus() {
        return ResponseEntity.ok(dataCatalogService.findByCatalogDescription("DEVICE_STATUS"));
    }
}
