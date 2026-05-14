package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.entity.Device;
import upc.ecovolt.mapping.dto.devicedto.DeviceMapper;
import upc.ecovolt.mapping.dto.devicedto.DeviceRequestDto;
import upc.ecovolt.mapping.dto.devicedto.DeviceResponseDto;
import upc.ecovolt.repository.DataCatalogoRepository;
import upc.ecovolt.repository.DeviceRepository;
import upc.ecovolt.repository.RoomRepository;
import upc.ecovolt.service.DeviceService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final RoomRepository roomRepository;
    private final DataCatalogoRepository dataCatalogoRepository;
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
        // 1. REGLA DE INTEGRIDAD: Validar que el ambiente exista
        var room = roomRepository.findById(requestDto.getRoomId())
                .orElseThrow(() -> new RuntimeException("Error: El ambiente (Room) no existe."));

        // 2. REGLA DE NEGOCIO SaaS: Validar límite del Plan del Usuario
        var user = room.getHome().getUser();
        int planLimit = user.getSubscriptionPlan().getDeviceLimit();
        long currentDeviceCount = deviceRepository.countByUserIdAndStatus(user.getId(), 1); // 1 = Activo

        if (currentDeviceCount >= planLimit) {
            throw new RuntimeException("Límite de plan alcanzado. Tu plan '" +
                    user.getSubscriptionPlan().getName() + "' solo permite " + planLimit + " dispositivos.");
        }

        // 3. REGLA TÉCNICA: Validar duplicidad de Serial Number (MAC Address)
        if (deviceRepository.findBySerialNumber(requestDto.getSerialNumber()).isPresent()) {
            throw new RuntimeException("El número de serie '" + requestDto.getSerialNumber() + "' ya está registrado.");
        }

        // 4. RESOLUCIÓN DE CATEGORÍA: DataCatalogo
        var category = dataCatalogoRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Error: La categoría seleccionada no existe."));

        log.info("Vinculando dispositivo {} al ambiente: {}", requestDto.getName(), room.getName());

        Device entity = deviceMapper.toEntity(requestDto);
        entity.setRoom(room);
        entity.setCategory(category);

        return deviceMapper.toResponseDto(deviceRepository.save(entity));
    }

    @Override
    @Transactional
    public DeviceResponseDto updateDevice(Long id, DeviceRequestDto requestDto) {
        return deviceRepository.findById(id).map(existingDevice -> {
            existingDevice.setName(requestDto.getName());
            existingDevice.setManufacturer(requestDto.getManufacturer());
            existingDevice.setFirmwareVersion(requestDto.getFirmwareVersion());

            // Actualizar categoría si cambió
            var category = dataCatalogoRepository.findById(requestDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            existingDevice.setCategory(category);

            // Nota: El serialNumber NO se debería actualizar por regla de hardware
            return deviceMapper.toResponseDto(deviceRepository.save(existingDevice));
        }).orElseThrow(() -> new RuntimeException("Dispositivo no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!deviceRepository.existsById(id)) throw new RuntimeException("El dispositivo no existe.");
        deviceRepository.deleteById(id);
    }

    // --- IMPLEMENTACIÓN DE MÉTODOS DE NEGOCIO ---

    @Override
    @Transactional(readOnly = true)
    public Optional<DeviceResponseDto> findBySerialNumber(String serialNumber) {
        return deviceRepository.findBySerialNumber(serialNumber).map(deviceMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceResponseDto> findByCategoryName(String categoryDescription) {
        var devices = deviceRepository.findByCategoryName(categoryDescription);
        return deviceMapper.toResponseDtoList(devices);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceResponseDto> findByRoomId(Long idRoom) {
        var devices = deviceRepository.findByRoomId(idRoom);
        return deviceMapper.toResponseDtoList(devices);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceResponseDto> findByHomeId(Long idHome) {
        // Navegación jerárquica: Muestra todos los equipos de la casa
        var devices = deviceRepository.findByHomeId(idHome);
        return deviceMapper.toResponseDtoList(devices);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceResponseDto> findByManufacturer(String manufacturer) {
        var devices = deviceRepository.findByManufacturer(manufacturer);
        return deviceMapper.toResponseDtoList(devices);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByUserIdAndStatus(Long idUser, Integer status) {
        return deviceRepository.countByUserIdAndStatus(idUser, status);
    }
}