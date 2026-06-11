package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
     * MÉTODO DE CIBERSEGURIDAD: Valida si el usuario es dueño del hardware
     * antes de ver o insertar telemetría.
     */
    private void validateDeviceOwnership(Long deviceId) {
        var principal = (UsuarioPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isStaff = principal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_ANALYST"));

        if (!isStaff) {
            Device device = deviceRepository.findById(deviceId)
                    .orElseThrow(() -> new RuntimeException("Error: Sensor no encontrado."));
            if (!device.getRoom().getHome().getUser().getIdUser().equals(principal.getIdUser())) {
                log.error("VIOLACIÓN DE PRIVACIDAD: El usuario {} intentó acceder a la telemetría del sensor ID: {}",
                        principal.getLogin(), deviceId);
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
    public Optional<EnergyReadingDto.Response> findReadingById(Long id) {
        // En un sistema real, primero buscaríamos el ID del dispositivo de esta lectura
        return readingRepository.findById(id).map(readingMapper::toResponseDto);
    }

    @Override
    @Transactional
    public EnergyReadingDto.Response saveReading(EnergyReadingDto.Request requestDto) {
        // CIBERSEGURIDAD: Validar que el equipo que envía el dato pertenece al usuario
        validateDeviceOwnership(requestDto.getDeviceId());

        log.info("IOT INGESTION: Registrando {}W para sensor ID: {}", requestDto.getWattage(), requestDto.getDeviceId());

        EnergyReading entity = readingMapper.toEntity(requestDto);
        var device = deviceRepository.findById(requestDto.getDeviceId()).get();
        entity.setDevice(device);

        // Las lecturas son inmutables, solo llevan fechaRegistro
        return readingMapper.toResponseDto(readingRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // Solo ADMIN (por interfaz)
        readingRepository.deleteById(id);
    }

    // --- INTELIGENCIA ENERGÉTICA CON VALIDACIÓN DE PROPIEDAD ---

    @Override
    @Transactional(readOnly = true)
    public BigDecimal sumWattageByDeviceAndPeriod(Long idDevice, LocalDateTime start, LocalDateTime end) {
        validateDeviceOwnership(idDevice);
        BigDecimal total = readingRepository.sumWattageByDeviceAndPeriod(idDevice, start, end);
        return (total != null) ? total : BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageVoltageByDevice(Long idDevice) {
        validateDeviceOwnership(idDevice);
        return readingRepository.getAverageVoltageByDevice(idDevice);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal sumTotalConsumptionByHome(Long idHome, LocalDateTime start, LocalDateTime end) {
        // Aquí se podría añadir validateHomeOwnership(idHome) similar al de HomeService
        BigDecimal total = readingRepository.sumTotalConsumptionByHome(idHome, start, end);
        return (total != null) ? total : BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnergyReadingDto.Response> findLatestReadingsByDevice(Long idDevice) {
        validateDeviceOwnership(idDevice);
        return readingMapper.toResponseDtoList(readingRepository.findLatestReadingsByDevice(idDevice));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnergyReadingDto.Response> findAbnormalConsumption(Long idDevice, BigDecimal threshold) {
        validateDeviceOwnership(idDevice);
        log.warn("AUDIT: Buscando fugas de energía en dispositivo {}", idDevice);
        return readingMapper.toResponseDtoList(readingRepository.findAbnormalConsumption(idDevice, threshold));
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal sumConsumptionByCategory(Long idHome, String categoryDescription, LocalDateTime start, LocalDateTime end) {
        // REGLA DE NEGOCIO: Reporte para Pie Chart
        BigDecimal total = readingRepository.sumConsumptionByCategory(idHome, categoryDescription, start, end);
        return (total != null) ? total : BigDecimal.ZERO;
    }
}
