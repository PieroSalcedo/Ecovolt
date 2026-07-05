package upc.ecovolt.mapping.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.DataCatalog;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataCatalogMapper extends GenericMapper<DataCatalog, DataCatalogDto.Request, DataCatalogDto.Response> {

    @Override
    @Mapping(target = "catalog.idCatalog", source = "idCatalog")
    DataCatalog toEntity(DataCatalogDto.Request requestDto);

    @Override
    @Mapping(target = "idCatalog", source = "catalog.idCatalog")
    @Mapping(target = "catalogName", source = "catalog.description")
    DataCatalogDto.Response toResponseDto(DataCatalog entity);
}
