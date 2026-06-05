package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.OptionDto;
import java.util.List;

public interface OptionService {

    List<OptionDto> findAll();

    OptionDto save(OptionDto requestDto);

    List<OptionDto> findByType(Integer type);

    List<OptionDto> findActiveOptions();
}