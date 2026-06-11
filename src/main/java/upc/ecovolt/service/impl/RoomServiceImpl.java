package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.entity.Home;
import upc.ecovolt.entity.Room;
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

    private void validateHomeOwnership(Long homeId) {
        var principal = (UsuarioPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            Home home = homeRepository.findById(homeId)
                    .orElseThrow(() -> new RuntimeException("Error: Vivienda no encontrada."));
            if (!home.getUser().getIdUser().equals(principal.getIdUser())) {
                log.error("ATAQUE DETECTADO: El usuario {} intento manipular ambientes en la Home ID: {}", principal.getLogin(), homeId);
                throw new RuntimeException("Acceso Denegado: No eres dueno de la propiedad principal.");
            }
        }
    }

    private void validateRoomOwnership(Long roomId) {
        var principal = (UsuarioPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            Room room = roomRepository.findById(roomId)
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
    public Optional<RoomDto.Response> findRoomById(Long id) {
        validateRoomOwnership(id);
        return roomRepository.findById(id).map(roomMapper::toResponseDto);
    }

    @Override
    @Transactional
    public RoomDto.Response saveRoom(RoomDto.Request requestDto) {
        validateHomeOwnership(requestDto.getHomeId());

        var home = homeRepository.findById(requestDto.getHomeId()).get();
        var roomType = dataCatalogRepository.findById(requestDto.getRoomTypeId())
                .orElseThrow(() -> new RuntimeException("Error: El Tipo de Ambiente no existe."));

        log.info("USER {}: Creando cuarto '{}' en la propiedad '{}'",
                home.getUser().getLogin(), requestDto.getName(), home.getAlias());

        Room entity = roomMapper.toEntity(requestDto);
        entity.setHome(home);
        entity.setRoomType(roomType);
        entity.setCreatedBy(home.getUser().getLogin());

        return roomMapper.toResponseDto(roomRepository.save(entity));
    }

    @Override
    @Transactional
    public RoomDto.Response updateRoom(Long id, RoomDto.Request requestDto) {
        validateRoomOwnership(id);
        validateHomeOwnership(requestDto.getHomeId());

        return roomRepository.findById(id).map(existingRoom -> {
            existingRoom.setName(requestDto.getName());
            existingRoom.setFloorNumber(requestDto.getFloorNumber());
            existingRoom.setOrientation(requestDto.getOrientation());
            existingRoom.setAreaSqm(requestDto.getAreaSqm());

            var roomType = dataCatalogRepository.findById(requestDto.getRoomTypeId())
                    .orElseThrow(() -> new RuntimeException("Tipo de ambiente no encontrado"));
            existingRoom.setRoomType(roomType);

            existingRoom.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
            return roomMapper.toResponseDto(roomRepository.save(existingRoom));
        }).orElseThrow(() -> new RuntimeException("Ambiente no encontrado"));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        validateRoomOwnership(id);
        roomRepository.deleteById(id);
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
    public List<RoomDto.Response> findByNameAndHome(String name, Long idHome) {
        validateHomeOwnership(idHome);
        return roomMapper.toResponseDtoList(roomRepository.findByNameAndHome(name, idHome));
    }
}
