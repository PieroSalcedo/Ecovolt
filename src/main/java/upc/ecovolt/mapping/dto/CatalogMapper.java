package upc.ecovolt.mapping.dto;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.Catalog;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CatalogMapper extends GenericMapper<Catalog, CatalogDto.Request, CatalogDto.Response> {
    // Los campos coinciden, no requiere @Mapping
}
