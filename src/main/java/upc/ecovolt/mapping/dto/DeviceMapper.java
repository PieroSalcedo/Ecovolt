package upc.ecovolt.mapping.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.Device;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeviceMapper extends GenericMapper<Device, DeviceDto.Request, DeviceDto.Response> {

    @Override
    @Mapping(target = "room.idRoom", source = "roomId")
    @Mapping(target = "category.idDataCatalog", source = "categoryId")
    Device toEntity(DeviceDto.Request requestDto);

    @Override
    @Mapping(target = "roomId", source = "room.idRoom")
    @Mapping(target = "roomName", source = "room.name")
    @Mapping(target = "categoryId", source = "category.idDataCatalog")
    @Mapping(target = "categoryDescription", source = "category.description")
    DeviceDto.Response toResponseDto(Device entity);
}
