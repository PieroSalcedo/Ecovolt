package upc.ecovolt.controller;

import upc.ecovolt.mapping.dto.roomdto.RoomRequestDto;
import upc.ecovolt.mapping.dto.roomdto.RoomResponseDto;
import upc.ecovolt.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Rooms", description = "Gestión de ambientes dentro de las viviendas")
@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @Operation(summary = "Listar todos los ambientes")
    @GetMapping
    public ResponseEntity<List<RoomResponseDto>> getAll() {
        return ResponseEntity.ok(roomService.findAllRooms());
    }

    @Operation(summary = "Obtener un ambiente por ID")
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponseDto> getById(@PathVariable Long id) {
        return roomService.findRoomById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Registrar un nuevo ambiente")
    @PostMapping
    public ResponseEntity<RoomResponseDto> create(@Valid @RequestBody RoomRequestDto request) {
        return new ResponseEntity<>(roomService.saveRoom(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar datos de un ambiente")
    @PutMapping("/{id}")
    public ResponseEntity<RoomResponseDto> update(@PathVariable Long id, @Valid @RequestBody RoomRequestDto request) {
        return ResponseEntity.ok(roomService.updateRoom(id, request));
    }

    @Operation(summary = "Eliminar un ambiente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roomService.delete(id);
        return ResponseEntity.noContent().build();
    }
}