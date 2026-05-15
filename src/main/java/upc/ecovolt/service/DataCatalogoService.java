package upc.ecovolt.service;

import org.springframework.security.access.prepost.PreAuthorize;
import upc.ecovolt.mapping.dto.datacatalogodto.DataCatalogoRequestDto;
import upc.ecovolt.mapping.dto.datacatalogodto.DataCatalogoResponseDto;
import java.util.List;

public interface DataCatalogoService {

    // --- CUALQUIER USUARIO LOGUEADO PUEDE LEER (Para llenar sus formularios) ---

    @PreAuthorize("isAuthenticated()")
    List<DataCatalogoResponseDto> findAll();

    @PreAuthorize("isAuthenticated()")
    List<DataCatalogoResponseDto> findByCatalogDescription(String catalogDescription);

    @PreAuthorize("isAuthenticated()")
    List<DataCatalogoResponseDto> findByDescriptionAndCatalog(String description, String catalogDescription);

    // --- SOLO EL STAFF PUEDE GESTIONAR LOS DICCIONARIOS ---

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    DataCatalogoResponseDto save(DataCatalogoRequestDto requestDto);
}