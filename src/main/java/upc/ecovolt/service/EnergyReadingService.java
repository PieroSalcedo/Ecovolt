package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.EnergyReadingDto;
import upc.ecovolt.mapping.dto.ReporteCasaDTO;
import upc.ecovolt.mapping.dto.ReporteCuartoDTO;
import upc.ecovolt.mapping.dto.ReporteDispositivoDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EnergyReadingService {

    List<EnergyReadingDto.Response> findAllReadings();

    List<ReporteCasaDTO> reporteConsumoPorCasa(Long idUser);

    List<ReporteCuartoDTO> reporteConsumoPorCuarto(Long idHome);

    List<ReporteDispositivoDTO> reporteConsumoPorDispositivo(Long idRoom);

    void delete(Long idReading);

    Optional<EnergyReadingDto.Response> findReadingById(Long idReading);

    EnergyReadingDto.Response saveReading(EnergyReadingDto.Request requestDto);

    BigDecimal sumWattageByDeviceAndPeriod(Long idDevice, LocalDateTime start, LocalDateTime end);

    Double getAverageVoltageByDevice(Long idDevice);

    BigDecimal sumTotalConsumptionByHome(Long idHome, LocalDateTime start, LocalDateTime end);

    List<EnergyReadingDto.Response> findLatestReadingsByDevice(Long idDevice, int limit);

    List<EnergyReadingDto.Response> findAbnormalConsumption(Long idDevice, BigDecimal threshold);

    BigDecimal sumConsumptionByCategory(Long idHome, String categoryDescription, LocalDateTime start, LocalDateTime end);
}