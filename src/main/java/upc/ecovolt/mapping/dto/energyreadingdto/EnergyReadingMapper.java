package upc.ecovolt.mapping.dto.energyreadingdto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.EnergyReading;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface EnergyReadingMapper {

    // ENTITY -> RESPONSE
    @Mapping(target = "deviceId", source = "device.id")
    @Mapping(target = "deviceName", source = "device.name") // <--- Valor agregado
    @Mapping(target = "createdAt", source = "fechaRegistro") // <--- SOLUCIÓN AL ERROR DE NOMBRES
    EnergyReadingResponseDto toResponseDto(EnergyReading entity);

    // REQUEST -> ENTITY
    @Mapping(target = "device.id", source = "deviceId")
    @Mapping(target = "fechaRegistro", ignore = true) // Se encarga el @PrePersist de la entidad
    EnergyReading toEntity(EnergyReadingRequestDto requestDto);

    List<EnergyReadingResponseDto> toResponseDtoList(List<EnergyReading> entityList);
}