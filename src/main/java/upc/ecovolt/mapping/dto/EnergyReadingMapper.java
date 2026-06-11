package upc.ecovolt.mapping.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.EnergyReading;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EnergyReadingMapper extends GenericMapper<EnergyReading, EnergyReadingDto.Request, EnergyReadingDto.Response> {

    @Override
    @Mapping(target = "device.idDevice", source = "deviceId")
    EnergyReading toEntity(EnergyReadingDto.Request requestDto);

    @Override
    @Mapping(target = "deviceId", source = "device.idDevice")
    @Mapping(target = "createdAt", source = "readingAt")
    EnergyReadingDto.Response toResponseDto(EnergyReading entity);
}
