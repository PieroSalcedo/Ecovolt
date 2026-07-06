package upc.ecovolt.mapping.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.Home;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HomeMapper extends GenericMapper<Home, HomeDto.Request, HomeDto.Response> {

    @Override
    @Mapping(target = "user.idUser", source = "idUser")
    @Mapping(target = "propertyType.idDataCatalog", source = "idPropertyType")
    Home toEntity(HomeDto.Request requestDto);

    @Override
    @Mapping(target = "ownerName", source = "user.login")
    // Usamos expresiones para evitar el NullPointerException si no hay tipo definido
    @Mapping(target = "propertyTypeName", expression = "java(entity.getPropertyType() != null ? entity.getPropertyType().getDescription() : \"Sin Tipo\")")
    @Mapping(target = "idPropertyType", expression = "java(entity.getPropertyType() != null ? entity.getPropertyType().getIdDataCatalog() : null)")
    HomeDto.Response toResponseDto(Home entity);
}
