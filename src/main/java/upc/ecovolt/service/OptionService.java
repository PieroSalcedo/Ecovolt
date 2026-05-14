package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.optiondto.OptionRequestDto;
import upc.ecovolt.mapping.dto.optiondto.OptionResponseDto;
import java.util.List;

public interface OptionService {
    List<OptionResponseDto> findAll();
    OptionResponseDto save(OptionRequestDto requestDto);

    // REGLA DE NEGOCIO
    List<OptionResponseDto> findByType(Integer tipo);
    List<OptionResponseDto> findActiveOptions();
}