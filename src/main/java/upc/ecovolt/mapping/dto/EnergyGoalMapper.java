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
    @Mapping(target = "room.idRoom", source = "idRoom")
    @Mapping(target = "device.idDevice", source = "idDevice")
    EnergyGoal toEntity(EnergyGoalDto.Request requestDto);

    @Override
    @Mapping(target = "targetValue", source = "monthlyLimitKwh")
    @Mapping(target = "targetName", expression = "java(entity.getHome() != null ? entity.getHome().getAlias() : (entity.getRoom() != null ? entity.getRoom().getName() : entity.getDevice().getName()))")
    @Mapping(target = "type", expression = "java(entity.getHome() != null ? \"CASA\" : (entity.getRoom() != null ? \"CUARTO\" : \"DISPOSITIVO\"))")
    EnergyGoalDto.Response toResponseDto(EnergyGoal entity);
}