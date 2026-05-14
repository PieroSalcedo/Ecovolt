package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.mapping.dto.optiondto.OptionMapper;
import upc.ecovolt.mapping.dto.optiondto.OptionRequestDto;
import upc.ecovolt.mapping.dto.optiondto.OptionResponseDto;
import upc.ecovolt.repository.OptionRepository;
import upc.ecovolt.service.OptionService;

import java.util.List;

@Service
@RequiredArgsConstructor
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
        var entity = optionMapper.toEntity(requestDto);
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