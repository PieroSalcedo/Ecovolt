package upc.ecovolt.mapping.dto.devicedto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.Device;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeviceMapper {

    // ENTITY -> RESPONSE
    @Mapping(target = "roomId", source = "room.id")
    @Mapping(target = "categoryName", source = "category.description") // <--- SOLUCIÓN AL ROJO
    DeviceResponseDto toResponseDto(Device entity);

    // REQUEST -> ENTITY
    @Mapping(target = "room.id", source = "roomId")
    @Mapping(target = "category", ignore = true) // <--- Se inyectará el objeto por ID en el Service
    Device toEntity(DeviceRequestDto requestDto);

    // Mapeo para listas (necesario para el findAll del Service)
    List<DeviceResponseDto> toResponseDtoList(List<Device> entityList);
}