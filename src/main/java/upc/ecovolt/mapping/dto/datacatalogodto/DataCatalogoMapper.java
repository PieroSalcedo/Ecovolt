package upc.ecovolt.mapping.dto.datacatalogodto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.DataCatalogo;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataCatalogoMapper {

    @Mapping(target = "idCatalog", source = "catalogo.idCatalog")
    @Mapping(target = "catalogDescription", source = "catalogo.description")
    DataCatalogoResponseDto toResponseDto(DataCatalogo entity);

    @Mapping(target = "catalogo.idCatalog", source = "idCatalog")
    DataCatalogo toEntity(DataCatalogoRequestDto requestDto);

    List<DataCatalogoResponseDto> toResponseDtoList(List<DataCatalogo> entityList);
}