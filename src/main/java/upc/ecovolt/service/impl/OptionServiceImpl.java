package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.mapping.dto.OptionDto;
import upc.ecovolt.mapping.dto.OptionMapper;
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
    public List<OptionDto.Response> findAll() {
        return optionMapper.toResponseDtoList(optionRepository.findAll());
    }

    @Override
    @Transactional
    public OptionDto.Response save(OptionDto.Request requestDto) {
        String route = requestDto.getRoute().trim();
        if (!route.startsWith("/")) {
            route = "/" + route;
        }

        if (!optionRepository.findByRoute(route).isEmpty()) {
            log.error("CONFIG ERROR: Ya existe una opcion registrada con la ruta {}", route);
            throw new RuntimeException("Error: La ruta de navegacion ya esta asignada a otra opcion.");
        }

        log.info("SISTEMA: Registrando nueva funcionalidad de interfaz: {}", requestDto.getName());

        var entity = optionMapper.toEntity(requestDto);
        entity.setRoute(route);
        entity.setStatus(1);

        return optionMapper.toResponseDto(optionRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OptionDto.Response> findByType(Integer type) {
        var options = optionRepository.findByType(type);
        return optionMapper.toResponseDtoList(options);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OptionDto.Response> findActiveOptions() {
        return optionMapper.toResponseDtoList(optionRepository.findActiveOptions());
    }
}
