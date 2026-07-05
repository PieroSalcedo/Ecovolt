package upc.ecovolt.mapping.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.EnergyGoal;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EnergyGoalMapper extends GenericMapper<EnergyGoal, EnergyGoalDto.Request, EnergyGoalDto.Response> {

    @Override
    @Mapping(target = "monthlyLimitKwh", source = "targetValue")
    @Mapping(target = "home.idHome", source = "idHome")
    EnergyGoal toEntity(EnergyGoalDto.Request requestDto);

    @Override
    @Mapping(target = "targetValue", source = "monthlyLimitKwh")
    @Mapping(target = "idHome", source = "home.idHome")
    @Mapping(target = "homeAddress", source = "home.address")
    EnergyGoalDto.Response toResponseDto(EnergyGoal entity);
}