package upc.ecovolt.service;

import org.springframework.security.access.prepost.PreAuthorize;
import upc.ecovolt.mapping.dto.devicedto.DeviceRequestDto;
import upc.ecovolt.mapping.dto.devicedto.DeviceResponseDto;

import java.util.List;
import java.util.Optional;

public interface DeviceService {

    // --- ACCESO ADMINISTRATIVO / SOPORTE ---
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT', 'AUDITOR')")
    List<DeviceResponseDto> findAllDevices();

    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    List<DeviceResponseDto> findByManufacturer(String manufacturer);

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    long countByUserIdAndStatus(Long idUser, Integer status);

    // --- ACCESO POR PROPIEDAD (OWNERSHIP) ---

    @PreAuthorize("isAuthenticated()")
    Optional<DeviceResponseDto> findDeviceById(Long id);

    @PreAuthorize("isAuthenticated()")
    DeviceResponseDto saveDevice(DeviceRequestDto requestDto);

    @PreAuthorize("isAuthenticated()")
    DeviceResponseDto updateDevice(Long id, DeviceRequestDto requestDto);

    @PreAuthorize("isAuthenticated()")
    void delete(Long id);

    @PreAuthorize("isAuthenticated()")
    Optional<DeviceResponseDto> findBySerialNumber(String serialNumber);

    @PreAuthorize("isAuthenticated()")
    List<DeviceResponseDto> findByCategoryName(String categoryDescription);

    @PreAuthorize("isAuthenticated()")
    List<DeviceResponseDto> findByRoomId(Long idRoom);

    @PreAuthorize("isAuthenticated()")
    List<DeviceResponseDto> findByHomeId(Long idHome);
}