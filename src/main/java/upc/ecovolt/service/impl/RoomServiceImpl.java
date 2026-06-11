package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.entity.Room;
import upc.ecovolt.entity.Home;
import upc.ecovolt.mapping.dto.RoomDto;
import upc.ecovolt.mapping.dto.RoomMapper;
import upc.ecovolt.repository.DataCatalogRepository;
import upc.ecovolt.repository.HomeRepository;
import upc.ecovolt.repository.RoomRepository;
import upc.ecovolt.security.UsuarioPrincipal;
import upc.ecovolt.service.RoomService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final HomeRepository homeRepository;
    private final DataCatalogRepository dataCatalogRepository;
    private final RoomMapper roomMapper;

    /**
     * CIBERSEGURIDAD: Valida propiedad sobre la vivienda principal.
     */
    private void validateHomeOwnership(Long idHome) {
        var principal = (UsuarioPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            Home home = homeRepository.findById(idHome)
                    .orElseThrow(() -> new RuntimeException("Error: Vivienda no encontrada."));
            if (!home.getUser().getIdUser().equals(principal.getIdUser())) {
                log.error("ACCESO NO AUTORIZADO: Usuario {} intentó acceder a ambientes de Home {}", principal.getLogin(), idHome);
                throw new RuntimeException("Acceso Denegado: No tienes permisos sobre esta propiedad.");
            }
        }
    }

    /**
     * CIBERSEGURIDAD: Valida propiedad sobre un ambiente específico.
     */
    private void validateRoomOwnership(Long idRoom) {
        var principal = (UsuarioPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            Room room = roomRepository.findById(idRoom)
                    .orElseThrow(() -> new RuntimeException("Error: Ambiente no encontrado."));
            if (!room.getHome().getUser().getIdUser().equals(principal.getIdUser())) {
                throw new RuntimeException("Acceso Denegado: Este ambiente no pertenece a tus propiedades.");
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDto.Response> findAllRooms() {
        return roomMapper.toResponseDtoList(roomRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoomDto.Response> findRoomById(Long idRoom) {
        validateRoomOwnership(idRoom);
        return roomRepository.findById(idRoom).map(roomMapper::toResponseDto);
    }

    @Override
    @Transactional
    public RoomDto.Response saveRoom(RoomDto.Request requestDto) {
        // CIBERSEGURIDAD: ¿La casa donde quieres crear el ambiente es tuya?
        validateHomeOwnership(requestDto.getIdHome());

        Home home = homeRepository.findById(requestDto.getIdHome())
                .orElseThrow(() -> new RuntimeException("Casa no encontrada"));

        log.info("REGISTRO AMBIENTE: Creando '{}' en '{}'", requestDto.getName(), home.getAlias());

        Room entity = roomMapper.toEntity(requestDto);
        entity.setHome(home);
        entity.setStatus(1); // Activo por defecto

        return roomMapper.toResponseDto(roomRepository.save(entity));
    }

    @Override
    @Transactional
    public RoomDto.Response updateRoom(Long idRoom, RoomDto.Request requestDto) {
        validateRoomOwnership(idRoom);
        if (requestDto.getIdHome() != null) validateHomeOwnership(requestDto.getIdHome());

        return roomRepository.findById(idRoom).map(existing -> {
            existing.setName(requestDto.getName());
            // Actualizar otros campos que vengan en el DTO (floorNumber, areaSqm, etc.)

            return roomMapper.toResponseDto(roomRepository.save(existing));
        }).orElseThrow(() -> new RuntimeException("Ambiente no encontrado"));
    }

    @Override
    @Transactional
    public void delete(Long idRoom) {
        validateRoomOwnership(idRoom);

        // REGLA DE NEGOCIO: Borrado lógico para no romper integridad con Dispositivos y Lecturas
        roomRepository.findById(idRoom).ifPresent(r -> {
            r.setStatus(0);
            roomRepository.save(r);
            log.warn("BORRADO LÓGICO: Ambiente ID {} desactivado", idRoom);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDto.Response> findByHomeId(Long idHome) {
        validateHomeOwnership(idHome);
        return roomMapper.toResponseDtoList(roomRepository.findByHomeId(idHome));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDto.Response> findByRoomTypeName(String typeDescription) {
        return roomMapper.toResponseDtoList(roomRepository.findByRoomTypeName(typeDescription));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDto.Response> findLargeRooms(BigDecimal minArea) {
        return roomMapper.toResponseDtoList(roomRepository.findLargeRooms(minArea));
    }

    @Override
    @Transactional(readOnly = true)
    public long countDevicesInRoom(Long idRoom) {
        validateRoomOwnership(idRoom);
        return roomRepository.countDevicesInRoom(idRoom);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDto.Response> findByHomeAndFloor(Long idHome, Integer floor) {
        validateHomeOwnership(idHome);
        return roomMapper.toResponseDtoList(roomRepository.findByHomeAndFloor(idHome, floor));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoomDto.Response> findByNameAndHome(String name, Long idHome) {
        validateHomeOwnership(idHome);
        return roomRepository.findByNameAndHome(name, idHome).map(roomMapper::toResponseDto);
    }
}