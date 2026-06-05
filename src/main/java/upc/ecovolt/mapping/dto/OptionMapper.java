package upc.ecovolt.mapping.dto;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.Option;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OptionMapper extends GenericMapper<Option, OptionDto.Request, OptionDto.Response> {
    // Mapeo directo: idOption, name, description, url y status coinciden
}
