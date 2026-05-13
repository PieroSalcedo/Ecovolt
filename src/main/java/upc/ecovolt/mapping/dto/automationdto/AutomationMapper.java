package upc.ecovolt.mapping.dto.automationdto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.Automation;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE // <--- IMPORTANTE
)
public interface AutomationMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "deviceId", source = "device.id")
    AutomationResponseDto toResponseDto(Automation entity);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "device.id", source = "deviceId")
    Automation toEntity(AutomationRequestDto requestDto);

    List<AutomationResponseDto> toResponseDtoList(List<Automation> entityList);
}
