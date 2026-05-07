package upc.ecovolt.controller;

import upc.ecovolt.mapping.dto.energyreadingdto.EnergyReadingRequestDto;
import upc.ecovolt.mapping.dto.energyreadingdto.EnergyReadingResponseDto;
import upc.ecovolt.service.EnergyReadingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Energy Readings", description = "Endpoints para la captura y visualización de telemetría eléctrica")
@RestController
@RequestMapping("/api/v1/energy-readings")
@RequiredArgsConstructor
public class EnergyReadingController {

    private final EnergyReadingService readingService;

    @Operation(summary = "Obtener todo el historial de lecturas")
    @GetMapping
    public ResponseEntity<List<EnergyReadingResponseDto>> getAll() {
        return ResponseEntity.ok(readingService.findAllReadings());
    }

    @Operation(summary = "Obtener una lectura específica por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<EnergyReadingResponseDto> getById(@PathVariable Long id) {
        return readingService.findReadingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Registrar una nueva lectura de telemetría (IoT Gateway)")
    @PostMapping
    public ResponseEntity<EnergyReadingResponseDto> create(@Valid @RequestBody EnergyReadingRequestDto request) {
        return new ResponseEntity<>(readingService.saveReading(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Eliminar una lectura del historial")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        readingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}