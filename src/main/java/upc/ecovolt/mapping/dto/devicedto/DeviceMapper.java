package upc.ecovolt.mapping.dto.devicedto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.Device;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE // <--- IMPORTANTE
)
public interface DeviceMapper {

    @Mapping(target = "roomId", source = "room.id")
    DeviceResponseDto toResponseDto(Device entity);

    @Mapping(target = "room.id", source = "roomId")
    Device toEntity(DeviceRequestDto requestDto);

    List<DeviceResponseDto> toResponseDtoList(List<Device> entityList);
}
