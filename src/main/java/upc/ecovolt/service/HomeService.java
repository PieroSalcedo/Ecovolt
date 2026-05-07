package upc.ecovolt.service;

import upc.ecovolt.mapping.dto.homedto.HomeRequestDto;
import upc.ecovolt.mapping.dto.homedto.HomeResponseDto;

import java.util.List;
import java.util.Optional;

public interface HomeService {
    List<HomeResponseDto> findAllHomes();
    Optional<HomeResponseDto> findHomeById(Long id);
    HomeResponseDto saveHome(HomeRequestDto requestDto);
    HomeResponseDto updateHome(Long id, HomeRequestDto requestDto);
    void delete(Long id);
}
