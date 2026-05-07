package upc.ecovolt.controller;

import upc.ecovolt.mapping.dto.homedto.HomeRequestDto;
import upc.ecovolt.mapping.dto.homedto.HomeResponseDto;
import upc.ecovolt.service.HomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Homes", description = "Gestión de viviendas y propiedades de EcoVolt")
@RestController
@RequestMapping("/api/v1/homes")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @Operation(summary = "Listar todas las viviendas")
    @GetMapping
    public ResponseEntity<List<HomeResponseDto>> getAll() {
        return ResponseEntity.ok(homeService.findAllHomes());
    }

    @Operation(summary = "Obtener una vivienda por ID")
    @GetMapping("/{id}")
    public ResponseEntity<HomeResponseDto> getById(@PathVariable Long id) {
        return homeService.findHomeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Registrar una nueva vivienda")
    @PostMapping
    public ResponseEntity<HomeResponseDto> create(@Valid @RequestBody HomeRequestDto request) {
        return new ResponseEntity<>(homeService.saveHome(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar datos de una vivienda")
    @PutMapping("/{id}")
    public ResponseEntity<HomeResponseDto> update(@PathVariable Long id, @Valid @RequestBody HomeRequestDto request) {
        return ResponseEntity.ok(homeService.updateHome(id, request));
    }

    @Operation(summary = "Eliminar una vivienda")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        homeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
