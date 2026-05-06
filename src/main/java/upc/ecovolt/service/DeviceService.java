package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.deviceDto.DeviceRequestDto;
import upc.ecovolt.mapping.dto.deviceDto.DeviceResponseDto;

import java.util.List;
import java.util.Optional;

public interface DeviceService {
    List<DeviceResponseDto> findAllDevices();
    Optional<DeviceResponseDto> findDeviceById(Long id);
    DeviceResponseDto saveDevice(DeviceRequestDto requestDto);
    DeviceResponseDto updateDevice(Long id, DeviceRequestDto requestDto);
    void delete(Long id);
}