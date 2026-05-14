package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.devicedto.DeviceRequestDto;
import upc.ecovolt.mapping.dto.devicedto.DeviceResponseDto;

import java.util.List;
import java.util.Optional;

public interface DeviceService {
    // CRUD Básico
    List<DeviceResponseDto> findAllDevices();
    Optional<DeviceResponseDto> findDeviceById(Long id);
    DeviceResponseDto saveDevice(DeviceRequestDto requestDto);
    DeviceResponseDto updateDevice(Long id, DeviceRequestDto requestDto);
    void delete(Long id);

    // MÉTODOS DE NEGOCIO (Provenientes del Repositorio)
    Optional<DeviceResponseDto> findBySerialNumber(String serialNumber);
    List<DeviceResponseDto> findByCategoryName(String categoryDescription);
    List<DeviceResponseDto> findByRoomId(Long idRoom);
    List<DeviceResponseDto> findByHomeId(Long idHome);
    List<DeviceResponseDto> findByManufacturer(String manufacturer);
    long countByUserIdAndStatus(Long idUser, Integer status);
}