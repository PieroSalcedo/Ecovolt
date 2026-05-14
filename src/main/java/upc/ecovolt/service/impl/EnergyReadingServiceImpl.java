package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.entity.EnergyReading;
import upc.ecovolt.mapping.dto.energyreadingdto.EnergyReadingMapper;
import upc.ecovolt.mapping.dto.energyreadingdto.EnergyReadingRequestDto;
import upc.ecovolt.mapping.dto.energyreadingdto.EnergyReadingResponseDto;
import upc.ecovolt.repository.DeviceRepository;
import upc.ecovolt.repository.EnergyReadingRepository;
import upc.ecovolt.service.EnergyReadingService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnergyReadingServiceImpl implements EnergyReadingService {

    private final EnergyReadingRepository readingRepository;
    private final DeviceRepository deviceRepository;
    private final EnergyReadingMapper readingMapper;

    @Override
    @Transactional(readOnly = true)
    public List<EnergyReadingResponseDto> findAllReadings() {
        return readingMapper.toResponseDtoList(readingRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EnergyReadingResponseDto> findReadingById(Long id) {
        return readingRepository.findById(id).map(readingMapper::toResponseDto);
    }

    @Override
    @Transactional
    public EnergyReadingResponseDto saveReading(EnergyReadingRequestDto requestDto) {
        // 1. REGLA DE INTEGRIDAD: Validar que el sensor físico esté registrado
        var device = deviceRepository.findById(requestDto.getDeviceId())
                .orElseThrow(() -> new RuntimeException("Error: El dispositivo no existe en el inventario."));

        log.info("Recibiendo telemetría IoT - Device: {} | Consumo: {}W", device.getSerialNumber(), requestDto.getWattage());

        EnergyReading entity = readingMapper.toEntity(requestDto);
        entity.setDevice(device); // Vinculamos la lectura al hardware

        return readingMapper.toResponseDto(readingRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!readingRepository.existsById(id)) throw new RuntimeException("Lectura no encontrada.");
        readingRepository.deleteById(id);
    }

    // --- IMPLEMENTACIÓN DE MÉTODOS DE INTELIGENCIA ENERGÉTICA ---

    @Override
    @Transactional(readOnly = true)
    public BigDecimal sumWattageByDeviceAndPeriod(Long idDevice, LocalDateTime start, LocalDateTime end) {
        // REGLA DE NEGOCIO: Base para el cálculo del recibo por dispositivo
        BigDecimal total = readingRepository.sumWattageByDeviceAndPeriod(idDevice, start, end);
        return (total != null) ? total : BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageVoltageByDevice(Long idDevice) {
        // REGLA DE NEGOCIO: Diagnóstico de salud de la red eléctrica
        return readingRepository.getAverageVoltageByDevice(idDevice);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal sumTotalConsumptionByHome(Long idHome, LocalDateTime start, LocalDateTime end) {
        // REGLA DE NEGOCIO: Consumo total de la vivienda para el Dashboard principal
        BigDecimal total = readingRepository.sumTotalConsumptionByHome(idHome, start, end);
        return (total != null) ? total : BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnergyReadingResponseDto> findLatestReadingsByDevice(Long idDevice) {
        // REGLA DE NEGOCIO: Monitor en tiempo real
        var readings = readingRepository.findLatestReadingsByDevice(idDevice);
        return readingMapper.toResponseDtoList(readings);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnergyReadingResponseDto> findAbnormalConsumption(Long idDevice, BigDecimal threshold) {
        // REGLA DE NEGOCIO: Detección de fugas de energía
        log.warn("Buscando anomalías de consumo en dispositivo ID: {}", idDevice);
        var readings = readingRepository.findAbnormalConsumption(idDevice, threshold);
        return readingMapper.toResponseDtoList(readings);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal sumConsumptionByCategory(Long idHome, String categoryDescription, LocalDateTime start, LocalDateTime end) {
        /*
         * REGLA DE NEGOCIO: El "Porqué" de Ecovolt.
         * Permite al usuario saber qué porcentaje de su dinero se va en 'Iluminación', 'AC', etc.
         */
        BigDecimal total = readingRepository.sumConsumptionByCategory(idHome, categoryDescription, start, end);
        return (total != null) ? total : BigDecimal.ZERO;
    }
}