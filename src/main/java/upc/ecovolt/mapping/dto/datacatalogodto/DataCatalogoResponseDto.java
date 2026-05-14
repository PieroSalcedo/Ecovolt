package upc.ecovolt.mapping.dto.datacatalogodto;

import lombok.Data;

@Data
public class DataCatalogoResponseDto {
    private Integer idDataCatalog;
    private String description;
    private Integer idCatalog;
    private String catalogDescription; // Ej: "ROOM_TYPES"
}