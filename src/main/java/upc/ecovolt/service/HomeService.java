package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.homedto.HomeRequestDto;
import upc.ecovolt.mapping.dto.homedto.HomeResponseDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface HomeService {
    // CRUD Básico
    List<HomeResponseDto> findAllHomes();
    Optional<HomeResponseDto> findHomeById(Long id);
    HomeResponseDto saveHome(HomeRequestDto requestDto);
    HomeResponseDto updateHome(Long id, HomeRequestDto requestDto);
    void delete(Long id);

    // MÉTODOS DE NEGOCIO (Basados en el Repositorio)
    List<HomeResponseDto> findActiveHomesByUser(Long idUser);
    List<HomeResponseDto> findByPropertyTypeName(String propertyTypeDescription);
    List<HomeResponseDto> findHomesByHighTariff(BigDecimal tariffThreshold);
    long countTotalDevicesByHome(Long idHome);
    List<HomeResponseDto> findByAliasAndUserId(String alias, Long idUser);
    List<HomeResponseDto> findByCity(String city);
}