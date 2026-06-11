package upc.ecovolt.mapping.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.Home;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HomeMapper extends GenericMapper<Home, HomeDto.Request, HomeDto.Response> {

    @Override
    @Mapping(target = "user.idUser", source = "userId")
    @Mapping(target = "propertyType.idDataCatalog", source = "propertyTypeId")
    Home toEntity(HomeDto.Request requestDto);

    @Override
    @Mapping(target = "userId", source = "user.idUser")
    @Mapping(target = "ownerName", source = "user.fullName")
    @Mapping(target = "propertyTypeId", source = "propertyType.idDataCatalog")
    @Mapping(target = "propertyTypeDescription", source = "propertyType.description")
    HomeDto.Response toResponseDto(Home entity);
}
