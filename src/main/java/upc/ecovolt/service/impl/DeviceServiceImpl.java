package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.entity.Device;
import upc.ecovolt.entity.Room;
import upc.ecovolt.mapping.dto.DeviceDto;
import upc.ecovolt.mapping.dto.DeviceMapper;
import upc.ecovolt.repository.DataCatalogRepository;
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
    private final DataCatalogRepository dataCatalogRepository;
    private final DeviceMapper deviceMapper;

    /**
     * CIBERSEGURIDAD: Valida si el ambiente pertenece al usuario logueado.
     */
    private void validateRoomOwnership(Long idRoom) {
        var principal = (UsuarioPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            Room room = roomRepository.findById(idRoom)
                    .orElseThrow(() -> new RuntimeException("Error: Ambiente no encontrado."));
            if (!room.getHome().getUser().getIdUser().equals(principal.getIdUser())) {
                log.error("ACCESO NO AUTORIZADO: Usuario {} intentó acceder a Habitación {}", principal.getLogin(), idRoom);
                throw new RuntimeException("Acceso Denegado: No tienes permisos sobre este ambiente.");
            }
        }
    }

    /**
     * CIBERSEGURIDAD: Valida propiedad sobre un dispositivo.
     */
    private void validateDeviceOwnership(Long idDevice) {
        var principal = (UsuarioPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            Device device = deviceRepository.findById(idDevice)
                    .orElseThrow(() -> new RuntimeException("Error: Dispositivo no encontrado."));
            if (!device.getRoom().getHome().getUser().getIdUser().equals(principal.getIdUser())) {
                throw new RuntimeException("Acceso Denegado: Este dispositivo no te pertenece.");
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceDto.Response> findAllDevices() {
        return deviceMapper.toResponseDtoList(deviceRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DeviceDto.Response> findDeviceById(Long idDevice) {
        validateDeviceOwnership(idDevice);
        return deviceRepository.findById(idDevice).map(deviceMapper::toResponseDto);
    }

    @Override
    @Transactional
    public DeviceDto.Response saveDevice(DeviceDto.Request requestDto) {
        // 1. CIBERSEGURIDAD: Validar propiedad del ambiente
        validateRoomOwnership(requestDto.getIdRoom());

        Room room = roomRepository.findById(requestDto.getIdRoom())
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada"));

        var user = room.getHome().getUser();

        // 2. REGLA DE NEGOCIO SaaS: Validar límite del Plan de Suscripción
        int planLimit = user.getSubscriptionPlan().getDeviceLimit();
        long currentDevices = deviceRepository.countByRoom_Home_User_IdUserAndStatus(user.getIdUser(), 1);

        if (currentDevices >= planLimit) {
            throw new RuntimeException("Límite de dispositivos alcanzado para el plan " + user.getSubscriptionPlan().getName());
        }

        // 3. REGLA TÉCNICA: Serial Único (Llave natural IoT)
        // Nota: En el DTO Request no tienes serialNumber, pero en la Entidad sí.
        // Si el sensor lo envía, deberías agregarlo al RequestDto.
        // Por ahora asumo que viene en el requestDto si lo agregas.

        Device entity = deviceMapper.toEntity(requestDto);
        entity.setRoom(room);
        entity.setStatus(1);

        return deviceMapper.toResponseDto(deviceRepository.save(entity));
    }

    @Override
    @Transactional
    public DeviceDto.Response updateDevice(Long idDevice, DeviceDto.Request requestDto) {
        validateDeviceOwnership(idDevice);
        if (requestDto.getIdRoom() != null) validateRoomOwnership(requestDto.getIdRoom());

        return deviceRepository.findById(idDevice).map(existing -> {
            existing.setName(requestDto.getName());
            // Si el DTO tuviera manufacturer o firmware, se actualizarían aquí

            if (requestDto.getIdRoom() != null) {
                Room newRoom = roomRepository.findById(requestDto.getIdRoom()).get();
                existing.setRoom(newRoom);
            }

            return deviceMapper.toResponseDto(deviceRepository.save(existing));
        }).orElseThrow(() -> new RuntimeException("Dispositivo no encontrado"));
    }

    @Override
    @Transactional
    public void delete(Long idDevice) {
        validateDeviceOwnership(idDevice);
        // REGLA DE NEGOCIO: Borrado lógico (Soft Delete) para preservar historial de lecturas
        deviceRepository.findById(idDevice).ifPresent(d -> {
            d.setStatus(0);
            deviceRepository.save(d);
            log.info("BORRADO LÓGICO: Dispositivo {} desactivado", idDevice);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DeviceDto.Response> findBySerialNumber(String serialNumber) {
        return deviceRepository.findBySerialNumber(serialNumber).map(deviceMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceDto.Response> findByCategoryName(String categoryDescription) {
        return deviceMapper.toResponseDtoList(deviceRepository.findByCategory_DescriptionAndStatus(categoryDescription, 1));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceDto.Response> findByRoomId(Long idRoom) {
        validateRoomOwnership(idRoom);
        return deviceMapper.toResponseDtoList(deviceRepository.findByRoom_IdRoomAndStatus(idRoom, 1));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceDto.Response> findByHomeId(Long idHome) {
        return deviceMapper.toResponseDtoList(deviceRepository.findByRoom_Home_IdHomeAndStatus(idHome, 1));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceDto.Response> findByManufacturer(String manufacturer) {
        return deviceMapper.toResponseDtoList(deviceRepository.findByManufacturerIgnoreCase(manufacturer));
    }

    @Override
    @Transactional(readOnly = true)
    public long countByUserIdAndStatus(Long idUser, Integer status) {
        return deviceRepository.countByRoom_Home_User_IdUserAndStatus(idUser, status);
    }
}