package upc.ecovolt.service;

import upc.ecovolt.entity.Home;
import upc.ecovolt.mapping.dto.HomeDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface HomeService {

    List<HomeDto.Response> consultaHomeDinamica(Long idUser, String alias, String city, int idTipo);

    List<HomeDto.Response> findAllHomes();

    List<HomeDto.Response> findByPropertyTypeName(String propertyTypeDescription);

    List<HomeDto.Response> findHomesByHighTariff(BigDecimal tariffThreshold);

    List<HomeDto.Response> findByCity(String city);

    List<HomeDto.Response> findActiveHomesByUser(Long idUser);

    HomeDto.Response saveHome(HomeDto.Request requestDto);

    Optional<HomeDto.Response> findHomeById(Long idHome);

    HomeDto.Response updateHome(Long idHome, HomeDto.Request requestDto);

    void delete(Long idHome);

    long countTotalDevicesByHome(Long idHome);

    Optional<HomeDto.Response> findByAliasAndUserId(String alias, Long idUser);
}