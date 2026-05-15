package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.entity.Option;
import upc.ecovolt.mapping.dto.optiondto.OptionMapper;
import upc.ecovolt.mapping.dto.optiondto.OptionRequestDto;
import upc.ecovolt.mapping.dto.optiondto.OptionResponseDto;
import upc.ecovolt.repository.OptionRepository;
import upc.ecovolt.service.OptionService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OptionServiceImpl implements OptionService {

    private final OptionRepository optionRepository;
    private final OptionMapper optionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<OptionResponseDto> findAll() {
        return optionMapper.toResponseDtoList(optionRepository.findAll());
    }

    @Override
    @Transactional
    public OptionResponseDto save(OptionRequestDto requestDto) {
        // 1. REGLA DE NEGOCIO: Normalización de rutas
        String ruta = requestDto.getRuta().trim();
        if (!ruta.startsWith("/")) {
            ruta = "/" + ruta;
        }

        // 2. VALIDACIÓN: Evitar colisión de rutas en el Frontend
        if (!optionRepository.findByRuta(ruta).isEmpty()) {
            log.error("CONFIG ERROR: Ya existe una opción registrada con la ruta {}", ruta);
            throw new RuntimeException("Error: La ruta de navegación ya está asignada a otra opción.");
        }

        log.info("SISTEMA: Registrando nueva funcionalidad de interfaz: {}", requestDto.getNombre());

        Option entity = optionMapper.toEntity(requestDto);
        entity.setRuta(ruta);
        entity.setEstado(1);

        return optionMapper.toResponseDto(optionRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OptionResponseDto> findByType(Integer tipo) {
        // REGLA DE NEGOCIO: Filtrar entre Menús (1) y Acciones (2)
        var options = optionRepository.findByType(tipo);
        return optionMapper.toResponseDtoList(options);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OptionResponseDto> findActiveOptions() {
        return optionMapper.toResponseDtoList(optionRepository.findActiveOptions());
    }
}