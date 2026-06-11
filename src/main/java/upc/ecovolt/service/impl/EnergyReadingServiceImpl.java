package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.entity.Device;
import upc.ecovolt.entity.EnergyReading;
import upc.ecovolt.mapping.dto.EnergyReadingDto;
import upc.ecovolt.mapping.dto.EnergyReadingMapper;
import upc.ecovolt.repository.DeviceRepository;
import upc.ecovolt.repository.EnergyReadingRepository;
import upc.ecovolt.security.UsuarioPrincipal;
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

    /**
     * CIBERSEGURIDAD: Valida si el usuario actual tiene acceso a la telemetría del hardware.
     */
    private void validateDeviceOwnership(Long idDevice) {
        var principal = (UsuarioPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isStaff = principal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_ANALYST"));

        if (!isStaff) {
            Device device = deviceRepository.findById(idDevice)
                    .orElseThrow(() -> new RuntimeException("Error: Sensor no encontrado."));
            if (!device.getRoom().getHome().getUser().getIdUser().equals(principal.getIdUser())) {
                log.error("VIOLACIÓN DE PRIVACIDAD: Usuario {} intentó acceder a datos del sensor {}",
                        principal.getLogin(), idDevice);
                throw new RuntimeException("Acceso denegado: No tienes permisos sobre este dispositivo.");
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnergyReadingDto.Response> findAllReadings() {
        return readingMapper.toResponseDtoList(readingRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EnergyReadingDto.Response> findReadingById(Long idReading) {
        return readingRepository.findById(idReading).map(readingMapper::toResponseDto);
    }

    @Override
    @Transactional
    public EnergyReadingDto.Response saveReading(EnergyReadingDto.Request requestDto) {
        // CIBERSEGURIDAD: Solo el dueño del dispositivo puede inyectar lecturas
        validateDeviceOwnership(requestDto.getIdDevice());

        log.debug("IOT INGESTION: {}W del sensor ID: {}", requestDto.getWattage(), requestDto.getIdDevice());

        EnergyReading entity = readingMapper.toEntity(requestDto);
        Device device = deviceRepository.findById(requestDto.getIdDevice())
                .orElseThrow(() -> new RuntimeException("Dispositivo no encontrado para la lectura."));

        entity.setDevice(device);
        entity.setStatus(1);

        return readingMapper.toResponseDto(readingRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long idReading) {
        // Auditoría mínima para borrado de telemetría (acción crítica)
        log.warn("AUDIT: Eliminando lectura de energía ID: {}", idReading);
        readingRepository.deleteById(idReading);
    }

    // --- MÉTODOS DE ANALÍTICA ENERGÉTICA ---

    @Override
    @Transactional(readOnly = true)
    public BigDecimal sumWattageByDeviceAndPeriod(Long idDevice, LocalDateTime start, LocalDateTime end) {
        validateDeviceOwnership(idDevice);
        return readingRepository.sumWattageByDeviceAndPeriod(idDevice, start, end)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageVoltageByDevice(Long idDevice) {
        validateDeviceOwnership(idDevice);
        return readingRepository.getAverageVoltageByDevice(idDevice)
                .orElse(0.0);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal sumTotalConsumptionByHome(Long idHome, LocalDateTime start, LocalDateTime end) {
        // La validación de propiedad de casa se asume en una capa superior o se puede añadir aquí
        return readingRepository.sumTotalConsumptionByHome(idHome, start, end)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnergyReadingDto.Response> findLatestReadingsByDevice(Long idDevice, int limit) {
        validateDeviceOwnership(idDevice);
        // Usamos PageRequest para limitar la carga al Frontend (ej. últimas 20 lecturas para el gráfico)
        var pageable = PageRequest.of(0, limit);
        var readings = readingRepository.findByDevice_IdDeviceOrderByReadingAtDesc(idDevice, pageable);
        return readingMapper.toResponseDtoList(readings);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnergyReadingDto.Response> findAbnormalConsumption(Long idDevice, BigDecimal threshold) {
        validateDeviceOwnership(idDevice);
        return readingMapper.toResponseDtoList(readingRepository.findAbnormalConsumption(idDevice, threshold));
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal sumConsumptionByCategory(Long idHome, String categoryDescription, LocalDateTime start, LocalDateTime end) {
        return readingRepository.sumConsumptionByCategory(idHome, categoryDescription, start, end)
                .orElse(BigDecimal.ZERO);
    }
}