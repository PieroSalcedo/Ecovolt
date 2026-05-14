package upc.ecovolt.mapping.dto.optiondto;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.Option;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OptionMapper {

    OptionResponseDto toResponseDto(Option entity);

    Option toEntity(OptionRequestDto requestDto);

    List<OptionResponseDto> toResponseDtoList(List<Option> entityList);
}