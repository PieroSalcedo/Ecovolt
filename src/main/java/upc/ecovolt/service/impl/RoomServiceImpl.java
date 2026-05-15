package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.entity.Room;
import upc.ecovolt.entity.Home;
import upc.ecovolt.mapping.dto.roomdto.RoomMapper;
import upc.ecovolt.mapping.dto.roomdto.RoomRequestDto;
import upc.ecovolt.mapping.dto.roomdto.RoomResponseDto;
import upc.ecovolt.repository.DataCatalogoRepository;
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
    private final DataCatalogoRepository dataCatalogoRepository;
    private final RoomMapper roomMapper;

    /**
     * MÉTODO DE CIBERSEGURIDAD: Valida si el usuario actual es dueño de la CASA
     * donde se encuentra (o se encontrará) el ambiente.
     */
    private void validateHomeOwnership(Long homeId) {
        var principal = (UsuarioPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            Home home = homeRepository.findById(homeId)
                    .orElseThrow(() -> new RuntimeException("Error: Vivienda no encontrada."));
            if (!home.getUser().getId().equals(principal.getIdUser())) {
                log.error("ATAQUE DETECTADO: El usuario {} intentó manipular ambientes en la Home ID: {}", principal.getLogin(), homeId);
                throw new RuntimeException("Acceso Denegado: No eres dueño de la propiedad principal.");
            }
        }
    }

    /**
     * MÉTODO DE CIBERSEGURIDAD: Valida propiedad directa sobre un AMBIENTE ya existente.
     */
    private void validateRoomOwnership(Long roomId) {
        var principal = (UsuarioPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new RuntimeException("Error: Ambiente no encontrado."));
            if (!room.getHome().getUser().getId().equals(principal.getIdUser())) {
                throw new RuntimeException("Acceso Denegado: Este ambiente no pertenece a tus propiedades.");
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponseDto> findAllRooms() {
        return roomMapper.toResponseDtoList(roomRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoomResponseDto> findRoomById(Long id) {
        validateRoomOwnership(id);
        return roomRepository.findById(id).map(roomMapper::toResponseDto);
    }

    @Override
    @Transactional
    public RoomResponseDto saveRoom(RoomRequestDto requestDto) {
        // CIBERSEGURIDAD: ¿La casa donde quieres crear el cuarto es tuya?
        validateHomeOwnership(requestDto.getHomeId());

        var home = homeRepository.findById(requestDto.getHomeId()).get();
        var roomType = dataCatalogoRepository.findById(requestDto.getRoomTypeId())
                .orElseThrow(() -> new RuntimeException("Error: El Tipo de Ambiente no existe."));

        log.info("USER {}: Creando cuarto '{}' en la propiedad '{}'",
                home.getUser().getLogin(), requestDto.getName(), home.getAlias());

        Room entity = roomMapper.toEntity(requestDto);
        entity.setHome(home);
        entity.setRoomType(roomType);
        entity.setUsuarioRegistro(home.getUser().getLogin());

        return roomMapper.toResponseDto(roomRepository.save(entity));
    }

    @Override
    @Transactional
    public RoomResponseDto updateRoom(Long id, RoomRequestDto requestDto) {
        validateRoomOwnership(id); // Validar antes de editar
        validateHomeOwnership(requestDto.getHomeId()); // Validar si la nueva casa (si cambió) es suya

        return roomRepository.findById(id).map(existingRoom -> {
            existingRoom.setName(requestDto.getName());
            existingRoom.setFloorNumber(requestDto.getFloorNumber());
            existingRoom.setOrientation(requestDto.getOrientation());
            existingRoom.setAreaSqm(requestDto.getAreaSqm());

            var roomType = dataCatalogoRepository.findById(requestDto.getRoomTypeId())
                    .orElseThrow(() -> new RuntimeException("Tipo de ambiente no encontrado"));
            existingRoom.setRoomType(roomType);

            existingRoom.setUsuarioActualizacion(SecurityContextHolder.getContext().getAuthentication().getName());
            return roomMapper.toResponseDto(roomRepository.save(existingRoom));
        }).orElseThrow(() -> new RuntimeException("Ambiente no encontrado"));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        validateRoomOwnership(id);
        roomRepository.deleteById(id);
    }

    // --- MÉTODOS DE NEGOCIO PROTEGIDOS ---

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponseDto> findByHomeId(Long idHome) {
        validateHomeOwnership(idHome);
        return roomMapper.toResponseDtoList(roomRepository.findByHomeId(idHome));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponseDto> findByRoomTypeName(String typeDescription) {
        return roomMapper.toResponseDtoList(roomRepository.findByRoomTypeName(typeDescription));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponseDto> findLargeRooms(BigDecimal minArea) {
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
    public List<RoomResponseDto> findByHomeAndFloor(Long idHome, Integer floor) {
        validateHomeOwnership(idHome);
        return roomMapper.toResponseDtoList(roomRepository.findByHomeAndFloor(idHome, floor));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponseDto> findByNameAndHome(String name, Long idHome) {
        validateHomeOwnership(idHome);
        return roomMapper.toResponseDtoList(roomRepository.findByNameAndHome(name, idHome));
    }
}