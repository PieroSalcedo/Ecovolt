package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.DeviceDto;

import java.util.List;
import java.util.Optional;

public interface DeviceService {

    List<DeviceDto> findAllDevices();

    List<DeviceDto> findByManufacturer(String manufacturer);

    long countByUserIdAndStatus(Long idUser, Integer status);

    Optional<DeviceDto> findDeviceById(Long id);

    DeviceDto saveDevice(DeviceDto requestDto);

    DeviceDto updateDevice(Long id, DeviceDto requestDto);

    void delete(Long id);

    Optional<DeviceDto> findBySerialNumber(String serialNumber);

    List<DeviceDto> findByCategoryName(String categoryDescription);

    List<DeviceDto> findByRoomId(Long idRoom);

    List<DeviceDto> findByHomeId(Long idHome);
}