package upc.ecovolt.service;

import org.springframework.security.access.prepost.PreAuthorize;
import upc.ecovolt.mapping.dto.homedto.HomeRequestDto;
import upc.ecovolt.mapping.dto.homedto.HomeResponseDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface HomeService {

    // --- ACCESO ADMINISTRATIVO (STAFF) ---
    @PreAuthorize("hasAnyRole('ADMIN', 'AUDITOR', 'ANALYST')")
    List<HomeResponseDto> findAllHomes();

    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'MANAGER')")
    List<HomeResponseDto> findByPropertyTypeName(String propertyTypeDescription);

    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    List<HomeResponseDto> findHomesByHighTariff(BigDecimal tariffThreshold);

    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    List<HomeResponseDto> findByCity(String city);

    // --- ACCESO DE CLIENTE (OWNERSHIP) ---

    // Un cliente solo puede ver sus propias casas
    @PreAuthorize("hasRole('ADMIN') or #idUser == principal.idUser")
    List<HomeResponseDto> findActiveHomesByUser(Long idUser);

    // El ID en el DTO debe ser igual al ID del token (Nadie registra casas para otros)
    @PreAuthorize("hasRole('ADMIN') or #requestDto.userId == principal.idUser")
    HomeResponseDto saveHome(HomeRequestDto requestDto);

    @PreAuthorize("isAuthenticated()") // La validación de dueño se hace dentro del Impl por seguridad
    Optional<HomeResponseDto> findHomeById(Long id);

    @PreAuthorize("isAuthenticated()")
    HomeResponseDto updateHome(Long id, HomeRequestDto requestDto);

    @PreAuthorize("hasRole('ADMIN') or isAuthenticated()")
    void delete(Long id);

    @PreAuthorize("isAuthenticated()")
    long countTotalDevicesByHome(Long idHome);

    @PreAuthorize("isAuthenticated()")
    List<HomeResponseDto> findByAliasAndUserId(String alias, Long idUser);
}