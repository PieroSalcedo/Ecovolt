package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.roomdto.RoomRequestDto;
import upc.ecovolt.mapping.dto.roomdto.RoomResponseDto;

import java.util.List;
import java.util.Optional;

public interface RoomService {
    List<RoomResponseDto> findAllRooms();
    Optional<RoomResponseDto> findRoomById(Long id);
    RoomResponseDto saveRoom(RoomRequestDto requestDto);
    RoomResponseDto updateRoom(Long id, RoomRequestDto requestDto);
    void delete(Long id);
}
