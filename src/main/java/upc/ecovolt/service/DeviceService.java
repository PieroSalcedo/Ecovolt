package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.DeviceDto;

import java.util.List;
import java.util.Optional;

public interface DeviceService {

    List<DeviceDto.Response> findAllDevices();

    List<DeviceDto.Response> findByManufacturer(String manufacturer);

    long countByUserIdAndStatus(Long idUser, Integer status);

    Optional<DeviceDto.Response> findDeviceById(Long idDevice);

    DeviceDto.Response saveDevice(DeviceDto.Request requestDto);

    DeviceDto.Response updateDevice(Long idDevice, DeviceDto.Request requestDto);

    void delete(Long idDevice);

    Optional<DeviceDto.Response> findBySerialNumber(String serialNumber);

    List<DeviceDto.Response> findByCategoryName(String categoryDescription);

    List<DeviceDto.Response> findByRoomId(Long idRoom);

    List<DeviceDto.Response> findByHomeId(Long idHome);
}