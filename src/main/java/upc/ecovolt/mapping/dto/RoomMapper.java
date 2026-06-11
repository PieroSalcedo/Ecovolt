package upc.ecovolt.mapping.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.Room;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapper extends GenericMapper<Room, RoomDto.Request, RoomDto.Response> {

    @Override
    @Mapping(target = "home.idHome", source = "homeId")
    @Mapping(target = "roomType.idDataCatalog", source = "roomTypeId")
    Room toEntity(RoomDto.Request requestDto);

    @Override
    @Mapping(target = "homeId", source = "home.idHome")
    @Mapping(target = "homeAddress", source = "home.address")
    @Mapping(target = "roomTypeId", source = "roomType.idDataCatalog")
    @Mapping(target = "roomTypeDescription", source = "roomType.description")
    RoomDto.Response toResponseDto(Room entity);
}
