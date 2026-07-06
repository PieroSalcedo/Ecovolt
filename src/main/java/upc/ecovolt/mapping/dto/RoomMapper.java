package upc.ecovolt.mapping.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.Room;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapper extends GenericMapper<Room, RoomDto.Request, RoomDto.Response> {

    @Override
    @Mapping(target = "home.idHome", source = "idHome")
    @Mapping(target = "roomType.idDataCatalog", source = "idRoomType")
    Room toEntity(RoomDto.Request requestDto);

    @Override
    @Mapping(target = "idHome", source = "home.idHome")
    @Mapping(target = "homeAddress", source = "home.alias") // Usamos el alias de la casa
    @Mapping(target = "idRoomType", source = "roomType.idDataCatalog")
    @Mapping(target = "roomTypeName", source = "roomType.description") // O .getValue() según tu BD
    RoomDto.Response toResponseDto(Room entity);
}
