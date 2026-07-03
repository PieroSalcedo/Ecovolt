package upc.ecovolt.mapping.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.Device;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeviceMapper extends GenericMapper<Device, DeviceDto.Request, DeviceDto.Response> {

    @Override
    @Mapping(target = "room.idRoom", source = "idRoom")
    @Mapping(target = "manufacturer", source = "brand")
    @Mapping(target = "category.idDataCatalog", source = "idCategory") // <-- MAPEA REQUEST A ENTIDAD
    Device toEntity(DeviceDto.Request requestDto);

    @Override
    @Mapping(target = "roomName", source = "room.name")
    @Mapping(target = "idRoom", source = "room.idRoom")
    @Mapping(target = "brand", source = "manufacturer")
    @Mapping(target = "idCategory", source = "category.idDataCatalog") // <-- MAPEA ENTIDAD A RESPONSE
    @Mapping(target = "categoryName", source = "category.description") // <-- Muestra el nombre de la categoría
    DeviceDto.Response toResponseDto(Device entity);
}