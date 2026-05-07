package upc.ecovolt.service.impl;

import upc.ecovolt.entity.Home;
import upc.ecovolt.mapping.dto.homedto.HomeMapper;
import upc.ecovolt.mapping.dto.homedto.HomeRequestDto;
import upc.ecovolt.mapping.dto.homedto.HomeResponseDto;
import upc.ecovolt.repository.HomeRepository;
import upc.ecovolt.repository.UserRepository;
import upc.ecovolt.service.HomeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final HomeRepository homeRepository;
    private final UserRepository userRepository; // Para validar existencia del dueÃ±o
    private final HomeMapper homeMapper;

    @Override
    @Transactional(readOnly = true)
    public List<HomeResponseDto> findAllHomes() {
        return homeMapper.toResponseDtoList(homeRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HomeResponseDto> findHomeById(Long id) {
        return homeRepository.findById(id).map(homeMapper::toResponseDto);
    }

    @Override
    @Transactional
    public HomeResponseDto saveHome(HomeRequestDto requestDto) {
        // Validamos que el usuario exista en la BD
        if (!userRepository.existsById(requestDto.getUserId())) {
            throw new RuntimeException("No se puede crear la vivienda: El usuario no existe.");
        }

        log.info("Registrando vivienda '{}' para el usuario ID: {}", requestDto.getAlias(), requestDto.getUserId());
        Home entity = homeMapper.toEntity(requestDto);
        return homeMapper.toResponseDto(homeRepository.save(entity));
    }

    @Override
    @Transactional
    public HomeResponseDto updateHome(Long id, HomeRequestDto requestDto) {
        return homeRepository.findById(id).map(existingHome -> {
            existingHome.setAddress(requestDto.getAddress());
            existingHome.setCity(requestDto.getCity());
            existingHome.setAlias(requestDto.getAlias());
            existingHome.setEnergyTariff(requestDto.getEnergyTariff());
            existingHome.setSquareMeters(requestDto.getSquareMeters());

            // Auditoría manual temporal
            existingHome.setUpdatedBy("ADMIN_USER");

            return homeMapper.toResponseDto(homeRepository.save(existingHome));
        }).orElseThrow(() -> new RuntimeException("Home not found with id: " + id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        homeRepository.deleteById(id);
    }
}