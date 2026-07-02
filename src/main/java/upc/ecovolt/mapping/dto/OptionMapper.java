package upc.ecovolt.mapping.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.Option;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OptionMapper extends GenericMapper<Option, OptionDto.Request, OptionDto.Response> {

    @Override
    @Mapping(target = "route", source = "route")
    @Mapping(target = "type", source = "type")
    OptionDto.Response toResponseDto(Option entity);
}