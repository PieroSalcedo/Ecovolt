package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.entity.DataCatalog;
import upc.ecovolt.entity.Device;
import upc.ecovolt.entity.Room;
import upc.ecovolt.entity.User;
import upc.ecovolt.mapping.dto.DeviceDto;
import upc.ecovolt.mapping.dto.DeviceMapper;
import upc.ecovolt.repository.DataCatalogRepository;
import upc.ecovolt.repository.DeviceRepository;
import upc.ecovolt.repository.RoomRepository;
import upc.ecovolt.repository.UserRepository;
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
    private final UserRepository userRepository;

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

    @Override
    @Transactional(readOnly = true)
    public List<DeviceDto.Response> findByUserId(Long idUser) {
        List<Device> entidades = deviceRepository.findByUserId(idUser);
        return deviceMapper.toResponseDtoList(entidades);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceDto.Response> consultaDispositivoDinamica(Long idHome, Long idRoom, String name) {
        List<Device> lista = deviceRepository.consultaDispositivoDinamica(idHome, idRoom, name);
        return deviceMapper.toResponseDtoList(lista);
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
    public long countActiveDevicesByUser(Long idUser) {
        return deviceRepository.countActiveDevicesByUser(idUser);
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
        // 1. Obtener el ID del usuario logueado desde el Token
        UsuarioPrincipal principal = (UsuarioPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long idLogueado = principal.getIdUser();

        // 2. Traer el usuario de la BD para ver su Plan y su Límite
        User usuario = userRepository.findById(idLogueado)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 3. Obtener el límite del plan (ej. 5) y cuántos tiene registrados (ej. 4)
        int limitePermitido = usuario.getSubscriptionPlan().getDeviceLimit();
        long dispositivosActuales = deviceRepository.countActiveDevicesByUser(idLogueado);

        log.info("Validando Plan: Usuario {} tiene {} de {} dispositivos permitidos",
                usuario.getLogin(), dispositivosActuales, limitePermitido);

        // 4. REGLA DE ORO: Si ya llegó al límite, lanzamos excepción
        if (dispositivosActuales >= limitePermitido) {
            throw new RuntimeException("Límite de dispositivos alcanzado. Tu plan '" +
                    usuario.getSubscriptionPlan().getName() + "' solo permite " +
                    limitePermitido + " equipos.");
        }

        // 5. Si todo está bien, guardamos el dispositivo normalmente
        Device entity = deviceMapper.toEntity(requestDto);
        entity.setStatus(1); // Activo

        Device saved = deviceRepository.save(entity);
        return deviceMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public DeviceDto.Response updateDevice(Long idDevice, DeviceDto.Request requestDto) {
        validateDeviceOwnership(idDevice);
        if (requestDto.getIdRoom() != null) validateRoomOwnership(requestDto.getIdRoom());

        return deviceRepository.findById(idDevice).map(existing -> {
            existing.setName(requestDto.getName());
            existing.setManufacturer(requestDto.getBrand());
            // Si el DTO tuviera manufacturer o firmware, se actualizarían aquí

            if (requestDto.getIdRoom() != null) {
                Room newRoom = roomRepository.findById(requestDto.getIdRoom()).get();
                existing.setRoom(newRoom);
            }

            if (requestDto.getIdCategory() != null && requestDto.getIdCategory() > 0) {
                DataCatalog category = new DataCatalog();
                category.setIdDataCatalog(requestDto.getIdCategory());
                existing.setCategory(category);
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
