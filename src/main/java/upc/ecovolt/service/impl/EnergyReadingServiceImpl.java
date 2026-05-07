package upc.ecovolt.service.impl;

import upc.ecovolt.entity.EnergyReading;
import upc.ecovolt.mapping.dto.energyreadingDto.EnergyReadingMapper;
import upc.ecovolt.mapping.dto.energyreadingDto.EnergyReadingRequestDto;
import upc.ecovolt.mapping.dto.energyreadingDto.EnergyReadingResponseDto;
import upc.ecovolt.repository.DeviceRepository;
import upc.ecovolt.repository.EnergyReadingRepository;
import upc.ecovolt.service.EnergyReadingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnergyReadingServiceImpl implements EnergyReadingService {

    private final EnergyReadingRepository readingRepository;
    private final DeviceRepository deviceRepository; // Para validación de jerarquía
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
        if (!deviceRepository.existsById(requestDto.getDeviceId())) {
            throw new RuntimeException("Error: El dispositivo con ID " + requestDto.getDeviceId() + " no existe.");
        }

        log.info("Procesando telemetría para dispositivo ID: {} ({} Watts)", requestDto.getDeviceId(), requestDto.getWattage());
        EnergyReading entity = readingMapper.toEntity(requestDto);
        return readingMapper.toResponseDto(readingRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        readingRepository.deleteById(id);
    }
}