package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.roomdto.RoomRequestDto;
import upc.ecovolt.mapping.dto.roomdto.RoomResponseDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface RoomService {
    // CRUD Básico
    List<RoomResponseDto> findAllRooms();
    Optional<RoomResponseDto> findRoomById(Long id);
    RoomResponseDto saveRoom(RoomRequestDto requestDto);
    RoomResponseDto updateRoom(Long id, RoomRequestDto requestDto);
    void delete(Long id);

    // MÉTODOS DE NEGOCIO (Provenientes del Repositorio)
    List<RoomResponseDto> findByHomeId(Long idHome);
    List<RoomResponseDto> findByRoomTypeName(String typeDescription);
    List<RoomResponseDto> findLargeRooms(BigDecimal minArea);
    long countDevicesInRoom(Long idRoom);
    List<RoomResponseDto> findByHomeAndFloor(Long idHome, Integer floor);
    List<RoomResponseDto> findByNameAndHome(String name, Long idHome);
}