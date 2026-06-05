package upc.ecovolt.mapping.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.Device;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeviceMapper extends GenericMapper<Device, DeviceDto.Request, DeviceDto.Response> {

    @Override
    @Mapping(target = "room.idRoom", source = "idRoom")
    Device toEntity(DeviceDto.Request requestDto);

    @Override
    @Mapping(target = "roomName", source = "room.name")
    DeviceDto.Response toResponseDto(Device entity);
}
