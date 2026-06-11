package upc.ecovolt.service;

import org.springframework.security.access.prepost.PreAuthorize;
import upc.ecovolt.mapping.dto.RoomDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface RoomService {

    // --- ACCESO ADMINISTRATIVO (STAFF) ---
    @PreAuthorize("hasAnyRole('ADMIN', 'AUDITOR', 'ANALYST')")
    List<RoomDto.Response> findAllRooms();

    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    List<RoomDto.Response> findByRoomTypeName(String typeDescription);

    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    List<RoomDto.Response> findLargeRooms(BigDecimal minArea);

    // --- ACCESO POR PROPIEDAD (OWNERSHIP) ---

    @PreAuthorize("isAuthenticated()")
    Optional<RoomDto.Response> findRoomById(Long id);

    @PreAuthorize("isAuthenticated()")
    RoomDto.Response saveRoom(RoomDto.Request requestDto);

    @PreAuthorize("isAuthenticated()")
    RoomDto.Response updateRoom(Long id, RoomDto.Request requestDto);

    @PreAuthorize("isAuthenticated()")
    void delete(Long id);

    @PreAuthorize("isAuthenticated()")
    List<RoomDto.Response> findByHomeId(Long idHome);

    @PreAuthorize("isAuthenticated()")
    long countDevicesInRoom(Long idRoom);

    @PreAuthorize("isAuthenticated()")
    List<RoomDto.Response> findByHomeAndFloor(Long idHome, Integer floor);

    @PreAuthorize("isAuthenticated()")
    List<RoomDto.Response> findByNameAndHome(String name, Long idHome);
}
