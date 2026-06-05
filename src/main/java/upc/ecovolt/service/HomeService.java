package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.HomeDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface HomeService {

    List<HomeDto> findAllHomes();

    List<HomeDto> findByPropertyTypeName(String propertyTypeDescription);

    List<HomeDto> findHomesByHighTariff(BigDecimal tariffThreshold);

    List<HomeDto> findByCity(String city);

    List<HomeDto> findActiveHomesByUser(Long idUser);

    HomeDto saveHome(HomeDto requestDto);

    Optional<HomeDto> findHomeById(Long idHome);

    HomeDto updateHome(Long idHome, HomeDto requestDto);

    void delete(Long id);

    long countTotalDevicesByHome(Long idHome);

    List<HomeDto> findByAliasAndUserId(String alias, Long idUser);
}