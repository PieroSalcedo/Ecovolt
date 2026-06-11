package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.RoomDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface RoomService {

    List<RoomDto.Response> findAllRooms();

    List<RoomDto.Response> findByRoomTypeName(String typeDescription);

    List<RoomDto.Response> findLargeRooms(BigDecimal minArea);

    Optional<RoomDto.Response> findRoomById(Long idRoom);

    RoomDto.Response saveRoom(RoomDto.Request requestDto);

    RoomDto.Response updateRoom(Long idRoom, RoomDto.Request requestDto);

    void delete(Long idRoom);

    List<RoomDto.Response> findByHomeId(Long idHome);

    long countDevicesInRoom(Long idRoom);

    List<RoomDto.Response> findByHomeAndFloor(Long idHome, Integer floor);

    Optional<RoomDto.Response> findByNameAndHome(String name, Long idHome);
}