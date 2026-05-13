package upc.ecovolt.mapping.dto.alertdto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.Alert;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE // <--- IMPORTANTE
)
public interface AlertMapper {
    @Mapping(target = "deviceId", source = "device.id")
    AlertResponseDto toResponseDto(Alert entity);

    @Mapping(target = "device.id", source = "deviceId")
    Alert toEntity(AlertRequestDto requestDto);

    List<AlertResponseDto> toResponseDtoList(List<Alert> entityList);
}
