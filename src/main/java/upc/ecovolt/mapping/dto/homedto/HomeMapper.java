package upc.ecovolt.mapping.dto.homedto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.Home;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE // <--- IMPORTANTE
)
public interface HomeMapper {

    @Mapping(target = "userId", source = "user.id")
    HomeResponseDto toResponseDto(Home entity);

    @Mapping(target = "user.id", source = "userId")
    Home toEntity(HomeRequestDto requestDto);

    List<HomeResponseDto> toResponseDtoList(List<Home> entityList);
}