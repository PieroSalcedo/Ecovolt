package upc.ecovolt.mapping.dto.roomdto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import upc.ecovolt.entity.Room;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(target = "homeId", source = "home.id")
    RoomResponseDto toResponseDto(Room entity);

    @Mapping(target = "home.id", source = "homeId")
    Room toEntity(RoomRequestDto requestDto);

    List<RoomResponseDto> toResponseDtoList(List<Room> entityList);
}