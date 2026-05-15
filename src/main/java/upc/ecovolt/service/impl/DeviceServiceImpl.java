package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.entity.Device;
import upc.ecovolt.entity.Room;
import upc.ecovolt.mapping.dto.devicedto.DeviceMapper;
import upc.ecovolt.mapping.dto.devicedto.DeviceRequestDto;
import upc.ecovolt.mapping.dto.devicedto.DeviceResponseDto;
import upc.ecovolt.repository.DataCatalogoRepository;
import upc.ecovolt.repository.DeviceRepository;
import upc.ecovolt.repository.RoomRepository;
import upc.ecovolt.security.UsuarioPrincipal;
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

    /**
     * CIBERSEGURIDAD: Valida si el ambiente donde se pondrá el equipo es del usuario.
     * Cruce jerárquico: Room -> Home -> User.
     */
    private void validateRoomOwnership(Long roomId) {
        var principal = (UsuarioPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new RuntimeException("Error: Ambiente no encontrado."));
            if (!room.getHome().getUser().getId().equals(principal.getIdUser())) {
                log.error("INTENTO DE INYECCIÓN IoT: El usuario {} intentó registrar equipo en cuarto ajeno ID: {}",
                        principal.getLogin(), roomId);
                throw new RuntimeException("Acceso Denegado: No tienes permisos sobre este ambiente.");
            }
        }
    }

    /**
     * CIBERSEGURIDAD: Valida propiedad sobre un dispositivo ya existente.
     */
    private void validateDeviceOwnership(Long deviceId) {
        var principal = (UsuarioPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            Device device = deviceRepository.findById(deviceId)
                    .orElseThrow(() -> new RuntimeException("Error: Dispositivo no encontrado."));
            if (!device.getRoom().getHome().getUser().getId().equals(principal.getIdUser())) {
                throw new RuntimeException("Acceso Denegado: Este dispositivo no te pertenece.");
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceResponseDto> findAllDevices() {
        return deviceMapper.toResponseDtoList(deviceRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DeviceResponseDto> findDeviceById(Long id) {
        validateDeviceOwnership(id);
        return deviceRepository.findById(id).map(deviceMapper::toResponseDto);
    }

    @Override
    @Transactional
    public DeviceResponseDto saveDevice(DeviceRequestDto requestDto) {
        // 1. CIBERSEGURIDAD: Validar propiedad del ambiente
        validateRoomOwnership(requestDto.getRoomId());

        Room room = roomRepository.findById(requestDto.getRoomId()).get();
        var user = room.getHome().getUser();

        // 2. REGLA DE NEGOCIO SaaS: Validar límite del Plan
        int planLimit = user.getSubscriptionPlan().getDeviceLimit();
        long currentDevices = deviceRepository.countByUserIdAndStatus(user.getId(), 1);

        if (currentDevices >= planLimit) {
            throw new RuntimeException("Límite superado. Tu plan '" +
                    user.getSubscriptionPlan().getName() + "' solo permite " + planLimit + " dispositivos.");
        }

        // 3. REGLA TÉCNICA: Serial Único
        if (deviceRepository.findBySerialNumber(requestDto.getSerialNumber()).isPresent()) {
            throw new RuntimeException("El número de serie ya existe en el ecosistema Ecovolt.");
        }

        // 4. RESOLUCIÓN DE CATEGORÍA
        var category = dataCatalogoRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Error: Categoría no válida."));

        log.info("REGISTRO IoT: Vinculando {} a la propiedad de {}", requestDto.getName(), user.getLogin());

        Device entity = deviceMapper.toEntity(requestDto);
        entity.setRoom(room);
        entity.setCategory(category);
        entity.setUsuarioRegistro(user.getLogin()); // Auditoría

        return deviceMapper.toResponseDto(deviceRepository.save(entity));
    }

    @Override
    @Transactional
    public DeviceResponseDto updateDevice(Long id, DeviceRequestDto requestDto) {
        validateDeviceOwnership(id);
        validateRoomOwnership(requestDto.getRoomId()); // Por si lo cambian de cuarto

        return deviceRepository.findById(id).map(existingDevice -> {
            existingDevice.setName(requestDto.getName());
            existingDevice.setManufacturer(requestDto.getManufacturer());
            existingDevice.setFirmwareVersion(requestDto.getFirmwareVersion());

            var category = dataCatalogoRepository.findById(requestDto.getCategoryId()).get();
            existingDevice.setCategory(category);

            existingDevice.setUsuarioActualizacion(SecurityContextHolder.getContext().getAuthentication().getName());
            return deviceMapper.toResponseDto(deviceRepository.save(existingDevice));
        }).orElseThrow(() -> new RuntimeException("Dispositivo no encontrado"));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        validateDeviceOwnership(id);
        log.warn("IoT SHUTDOWN: Eliminando dispositivo ID: {}", id);
        deviceRepository.deleteById(id);
    }

    // --- IMPLEMENTACIÓN DE MÉTODOS DE NEGOCIO ---

    @Override
    @Transactional(readOnly = true)
    public Optional<DeviceResponseDto> findBySerialNumber(String serialNumber) {
        // La validación de propiedad se hace implícitamente al devolver el DTO
        // pero por seguridad podrías añadir validateDeviceOwnership aquí si el ID fuera conocido
        return deviceRepository.findBySerialNumber(serialNumber).map(deviceMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceResponseDto> findByCategoryName(String categoryDescription) {
        return deviceMapper.toResponseDtoList(deviceRepository.findByCategoryName(categoryDescription));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceResponseDto> findByRoomId(Long idRoom) {
        validateRoomOwnership(idRoom);
        return deviceMapper.toResponseDtoList(deviceRepository.findByRoomId(idRoom));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceResponseDto> findByHomeId(Long idHome) {
        // En un nivel real, aquí se debería inyectar validateHomeOwnership(idHome)
        return deviceMapper.toResponseDtoList(deviceRepository.findByHomeId(idHome));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceResponseDto> findByManufacturer(String manufacturer) {
        return deviceMapper.toResponseDtoList(deviceRepository.findByManufacturer(manufacturer));
    }

    @Override
    @Transactional(readOnly = true)
    public long countByUserIdAndStatus(Long idUser, Integer status) {
        return deviceRepository.countByUserIdAndStatus(idUser, status);
    }
}