package upc.ecovolt.mapping.dto.roomdto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.Room;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RoomMapper {

    // ENTITY -> RESPONSE
    @Mapping(target = "homeId", source = "home.id")
    @Mapping(target = "roomTypeName", source = "roomType.description") // <--- SOLUCIÓN AL ERROR
    RoomResponseDto toResponseDto(Room entity);

    // REQUEST -> ENTITY
    @Mapping(target = "home.id", source = "homeId")
    @Mapping(target = "roomType", ignore = true) // <--- Se buscará por ID en el Service
    Room toEntity(RoomRequestDto requestDto);

    List<RoomResponseDto> toResponseDtoList(List<Room> entityList);
}