package upc.ecovolt.service.impl;

import upc.ecovolt.entity.Device;
import upc.ecovolt.mapping.dto.deviceDto.DeviceMapper;
import upc.ecovolt.mapping.dto.deviceDto.DeviceRequestDto;
import upc.ecovolt.mapping.dto.deviceDto.DeviceResponseDto;
import upc.ecovolt.repository.DeviceRepository;
import upc.ecovolt.repository.RoomRepository;
import upc.ecovolt.service.DeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final RoomRepository roomRepository; // Para validar jerarquÃa
    private final DeviceMapper deviceMapper;

    @Override
    @Transactional(readOnly = true)
    public List<DeviceResponseDto> findAllDevices() {
        return deviceMapper.toResponseDtoList(deviceRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DeviceResponseDto> findDeviceById(Long id) {
        return deviceRepository.findById(id).map(deviceMapper::toResponseDto);
    }

    @Override
    @Transactional
    public DeviceResponseDto saveDevice(DeviceRequestDto requestDto) {
        if (!roomRepository.existsById(requestDto.getRoomId())) {
            throw new RuntimeException("Error: El ambiente (Room) con ID " + requestDto.getRoomId() + " no existe.");
        }

        log.info("Vinculando dispositivo serial: {} al ambiente ID: {}", requestDto.getSerialNumber(), requestDto.getRoomId());
        Device entity = deviceMapper.toEntity(requestDto);
        return deviceMapper.toResponseDto(deviceRepository.save(entity));
    }

    @Override
    @Transactional
    public DeviceResponseDto updateDevice(Long id, DeviceRequestDto requestDto) {
        return deviceRepository.findById(id).map(existing -> {
            existing.setName(requestDto.getName());
            existing.setCategory(requestDto.getCategory());
            existing.setManufacturer(requestDto.getManufacturer());
            existing.setFirmwareVersion(requestDto.getFirmwareVersion());
            // Nota: El serial_number usualmente no se cambia una vez registrado, pero se puede incluir si se desea.

            return deviceMapper.toResponseDto(deviceRepository.save(existing));
        }).orElseThrow(() -> new RuntimeException("Device not found with id: " + id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        deviceRepository.deleteById(id);
    }
}