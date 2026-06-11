package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.OptionDto;
import java.util.List;

public interface OptionService {

    List<OptionDto.Response> findAll();

    OptionDto.Response save(OptionDto.Request requestDto);

    List<OptionDto.Response> findByType(Integer type);

    List<OptionDto.Response> findActiveOptions();
}
