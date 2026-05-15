package upc.ecovolt.service;

import org.springframework.security.access.prepost.PreAuthorize;
import upc.ecovolt.mapping.dto.optiondto.OptionRequestDto;
import upc.ecovolt.mapping.dto.optiondto.OptionResponseDto;
import java.util.List;

public interface OptionService {

    @PreAuthorize("hasRole('ADMIN')") // Solo tú puedes ver la configuración total
    List<OptionResponseDto> findAll();

    @PreAuthorize("hasRole('ADMIN')") // REGLA CRÍTICA: Solo el Admin crea rutas de sistema
    OptionResponseDto save(OptionRequestDto requestDto);

    @PreAuthorize("isAuthenticated()") // Cualquier usuario logueado necesita esto para armar su menú
    List<OptionResponseDto> findByType(Integer tipo);

    @PreAuthorize("isAuthenticated()")
    List<OptionResponseDto> findActiveOptions();
}