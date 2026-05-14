package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.entity.Room;
import upc.ecovolt.mapping.dto.roomdto.RoomMapper;
import upc.ecovolt.mapping.dto.roomdto.RoomRequestDto;
import upc.ecovolt.mapping.dto.roomdto.RoomResponseDto;
import upc.ecovolt.repository.DataCatalogoRepository;
import upc.ecovolt.repository.HomeRepository;
import upc.ecovolt.repository.RoomRepository;
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
    private final DataCatalogoRepository dataCatalogoRepository; // Para resolver el tipo de habitación
    private final RoomMapper roomMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponseDto> findAllRooms() {
        return roomMapper.toResponseDtoList(roomRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoomResponseDto> findRoomById(Long id) {
        return roomRepository.findById(id).map(roomMapper::toResponseDto);
    }

    @Override
    @Transactional
    public RoomResponseDto saveRoom(RoomRequestDto requestDto) {
        // 1. REGLA DE NEGOCIO: Validar que la vivienda exista
        var home = homeRepository.findById(requestDto.getHomeId())
                .orElseThrow(() -> new RuntimeException("Error: La vivienda (Home) no existe."));

        // 2. REGLA TÉCNICA: Resolver el Tipo de Habitación desde DataCatalogo
        var roomType = dataCatalogoRepository.findById(requestDto.getRoomTypeId())
                .orElseThrow(() -> new RuntimeException("Error: El Tipo de Habitación (Catálogo) no existe."));

        log.info("Creando ambiente '{}' para la vivienda ID: {}", requestDto.getName(), home.getId());

        Room entity = roomMapper.toEntity(requestDto);
        entity.setHome(home);
        entity.setRoomType(roomType); // Inyectamos el objeto del catálogo

        return roomMapper.toResponseDto(roomRepository.save(entity));
    }

    @Override
    @Transactional
    public RoomResponseDto updateRoom(Long id, RoomRequestDto requestDto) {
        return roomRepository.findById(id).map(existingRoom -> {
            existingRoom.setName(requestDto.getName());
            existingRoom.setFloorNumber(requestDto.getFloorNumber());
            existingRoom.setOrientation(requestDto.getOrientation());
            existingRoom.setAreaSqm(requestDto.getAreaSqm());

            // Actualizar relaciones
            var home = homeRepository.findById(requestDto.getHomeId())
                    .orElseThrow(() -> new RuntimeException("Vivienda no encontrada"));
            var roomType = dataCatalogoRepository.findById(requestDto.getRoomTypeId())
                    .orElseThrow(() -> new RuntimeException("Tipo de ambiente no encontrado"));

            existingRoom.setHome(home);
            existingRoom.setRoomType(roomType);

            return roomMapper.toResponseDto(roomRepository.save(existingRoom));
        }).orElseThrow(() -> new RuntimeException("Ambiente no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!roomRepository.existsById(id)) throw new RuntimeException("El ambiente no existe.");
        roomRepository.deleteById(id);
    }

    // --- IMPLEMENTACIÓN DE MÉTODOS DE NEGOCIO ---

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponseDto> findByHomeId(Long idHome) {
        // Lista habitaciones de una casa (Útil para el menú de navegación interna)
        var rooms = roomRepository.findByHomeId(idHome);
        return roomMapper.toResponseDtoList(rooms);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponseDto> findByRoomTypeName(String typeDescription) {
        // Ejemplo: "Traer todas las COCINAS para análisis de eficiencia"
        var rooms = roomRepository.findByRoomTypeName(typeDescription);
        return roomMapper.toResponseDtoList(rooms);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponseDto> findLargeRooms(BigDecimal minArea) {
        // Identifica áreas grandes que podrían estar desperdiciando energía
        var rooms = roomRepository.findLargeRooms(minArea);
        return roomMapper.toResponseDtoList(rooms);
    }

    @Override
    @Transactional(readOnly = true)
    public long countDevicesInRoom(Long idRoom) {
        // REGLA DE NEGOCIO: Auditoría de carga IoT por habitación
        return roomRepository.countDevicesInRoom(idRoom);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponseDto> findByHomeAndFloor(Long idHome, Integer floor) {
        var rooms = roomRepository.findByHomeAndFloor(idHome, floor);
        return roomMapper.toResponseDtoList(rooms);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponseDto> findByNameAndHome(String name, Long idHome) {
        var rooms = roomRepository.findByNameAndHome(name, idHome);
        return roomMapper.toResponseDtoList(rooms);
    }
}