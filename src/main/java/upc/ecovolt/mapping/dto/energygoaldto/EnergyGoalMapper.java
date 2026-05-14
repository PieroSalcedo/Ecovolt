package upc.ecovolt.mapping.dto.energygoaldto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.EnergyGoal;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EnergyGoalMapper {

    @Mapping(target = "homeId", source = "home.id")
    @Mapping(target = "homeAlias", source = "home.alias") // Agregamos el nombre de la casa
    EnergyGoalResponseDto toResponseDto(EnergyGoal entity);

    @Mapping(target = "home.id", source = "homeId")
    EnergyGoal toEntity(EnergyGoalRequestDto requestDto);

    List<EnergyGoalResponseDto> toResponseDtoList(List<EnergyGoal> entityList);
}