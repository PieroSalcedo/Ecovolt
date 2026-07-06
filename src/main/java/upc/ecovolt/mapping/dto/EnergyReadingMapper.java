package upc.ecovolt.mapping.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.EnergyReading;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EnergyReadingMapper extends GenericMapper<EnergyReading, EnergyReadingDto.Request, EnergyReadingDto.Response> {

    @Override
    @Mapping(target = "device.idDevice", source = "idDevice")
    EnergyReading toEntity(EnergyReadingDto.Request requestDto);

    @Override
    // Ahora esta línea ya no fallará porque idDevice existe en el Response
    @Mapping(target = "idDevice", source = "device.idDevice")
    EnergyReadingDto.Response toResponseDto(EnergyReading entity);
}