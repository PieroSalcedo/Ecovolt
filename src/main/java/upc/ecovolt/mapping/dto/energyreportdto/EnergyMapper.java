package upc.ecovolt.mapping.dto.energyreportdto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.EnergyReport;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface EnergyMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "homeId", source = "home.id")
    EnergyResponseDto toResponseDto(EnergyReport entity);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "home.id", source = "homeId")
    EnergyReport toEntity(EnergyRequestDto requestDto);

    List<EnergyResponseDto> toResponseDtoList(List<EnergyReport> entityList);
}
