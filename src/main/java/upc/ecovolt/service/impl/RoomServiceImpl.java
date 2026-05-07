package upc.ecovolt.service.impl;

import upc.ecovolt.entity.Room;
import upc.ecovolt.mapping.dto.roomdto.RoomMapper;
import upc.ecovolt.mapping.dto.roomdto.RoomRequestDto;
import upc.ecovolt.mapping.dto.roomdto.RoomResponseDto;
import upc.ecovolt.repository.HomeRepository;
import upc.ecovolt.repository.RoomRepository;
import upc.ecovolt.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final HomeRepository homeRepository;
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
        if (!homeRepository.existsById(requestDto.getHomeId())) {
            throw new RuntimeException("Error: La vivienda (Home) con ID " + requestDto.getHomeId() + " no existe.");
        }

        log.info("Creando ambiente '{}' para la vivienda ID: {}", requestDto.getName(), requestDto.getHomeId());
        Room entity = roomMapper.toEntity(requestDto);
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
            existingRoom.setRoomType(requestDto.getRoomType());

            // Auditoría manual temporal
            existingRoom.setUpdatedBy("ADMIN_USER");

            return roomMapper.toResponseDto(roomRepository.save(existingRoom));
        }).orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        roomRepository.deleteById(id);
    }
}