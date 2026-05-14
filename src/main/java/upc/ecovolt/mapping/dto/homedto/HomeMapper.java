package upc.ecovolt.mapping.dto.homedto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.Home;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface HomeMapper {

    // ENTITY -> RESPONSE
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "propertyTypeName", source = "propertyType.description") // <--- SOLUCIÓN AL ERROR
    HomeResponseDto toResponseDto(Home entity);

    // REQUEST -> ENTITY
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "propertyType", ignore = true) // <--- Se asigna por ID en el Service
    Home toEntity(HomeRequestDto requestDto);

    List<HomeResponseDto> toResponseDtoList(List<Home> entityList);
}