package upc.ecovolt.mapping.dto.energyreadingdto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import upc.ecovolt.entity.EnergyReading;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EnergyReadingMapper {

    @Mapping(target = "deviceId", source = "device.id")
    EnergyReadingResponseDto toResponseDto(EnergyReading entity);

    @Mapping(target = "device.id", source = "deviceId")
    EnergyReading toEntity(EnergyReadingRequestDto requestDto);

    List<EnergyReadingResponseDto> toResponseDtoList(List<EnergyReading> entityList);
}