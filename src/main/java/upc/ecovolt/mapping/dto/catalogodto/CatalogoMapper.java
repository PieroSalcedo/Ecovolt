package upc.ecovolt.mapping.dto.catalogodto;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.Catalogo;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CatalogoMapper {
    CatalogoResponseDto toResponseDto(Catalogo entity);
    Catalogo toEntity(CatalogoRequestDto requestDto);
    List<CatalogoResponseDto> toResponseDtoList(List<Catalogo> entityList);
}