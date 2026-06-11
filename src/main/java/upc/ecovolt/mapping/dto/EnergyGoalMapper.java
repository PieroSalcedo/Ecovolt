package upc.ecovolt.mapping.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.EnergyGoal;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EnergyGoalMapper extends GenericMapper<EnergyGoal, EnergyGoalDto.Request, EnergyGoalDto.Response> {

    @Override
    @Mapping(target = "home.idHome", source = "homeId")
    EnergyGoal toEntity(EnergyGoalDto.Request requestDto);

    @Override
    @Mapping(target = "homeId", source = "home.idHome")
    @Mapping(target = "homeAlias", source = "home.alias")
    @Mapping(target = "homeAddress", source = "home.address")
    EnergyGoalDto.Response toResponseDto(EnergyGoal entity);
}
