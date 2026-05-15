package upc.ecovolt.service;

import org.springframework.security.access.prepost.PreAuthorize;
import upc.ecovolt.mapping.dto.roomdto.RoomRequestDto;
import upc.ecovolt.mapping.dto.roomdto.RoomResponseDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface RoomService {

    // --- ACCESO ADMINISTRATIVO (STAFF) ---
    @PreAuthorize("hasAnyRole('ADMIN', 'AUDITOR', 'ANALYST')")
    List<RoomResponseDto> findAllRooms();

    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    List<RoomResponseDto> findByRoomTypeName(String typeDescription);

    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    List<RoomResponseDto> findLargeRooms(BigDecimal minArea);

    // --- ACCESO POR PROPIEDAD (OWNERSHIP) ---

    @PreAuthorize("isAuthenticated()")
    Optional<RoomResponseDto> findRoomById(Long id);

    @PreAuthorize("isAuthenticated()")
    RoomResponseDto saveRoom(RoomRequestDto requestDto);

    @PreAuthorize("isAuthenticated()")
    RoomResponseDto updateRoom(Long id, RoomRequestDto requestDto);

    @PreAuthorize("isAuthenticated()")
    void delete(Long id);

    @PreAuthorize("isAuthenticated()")
    List<RoomResponseDto> findByHomeId(Long idHome);

    @PreAuthorize("isAuthenticated()")
    long countDevicesInRoom(Long idRoom);

    @PreAuthorize("isAuthenticated()")
    List<RoomResponseDto> findByHomeAndFloor(Long idHome, Integer floor);

    @PreAuthorize("isAuthenticated()")
    List<RoomResponseDto> findByNameAndHome(String name, Long idHome);
}