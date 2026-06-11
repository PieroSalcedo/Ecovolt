package upc.ecovolt.mapping.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.Home;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HomeMapper extends GenericMapper<Home, HomeDto.Request, HomeDto.Response> {

    @Override
    @Mapping(target = "user.idUser", source = "idUser")
    Home toEntity(HomeDto.Request requestDto);

    @Override
    @Mapping(target = "ownerName", source = "user.firstName")
    HomeDto.Response toResponseDto(Home entity);
}
