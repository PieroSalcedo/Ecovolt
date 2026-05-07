package upc.ecovolt.service.impl;

import upc.ecovolt.mapping.dto.userDto.UserMapper;
import upc.ecovolt.mapping.dto.userDto.UserRequestDto;
import upc.ecovolt.mapping.dto.userDto.UserResponseDto;
import upc.ecovolt.repository.UserRepository;
import upc.ecovolt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserResponseDto> findAllUsers() {
        var users = userRepository.findAll();
        return userMapper.toDtoList(users);
    }

    @Override
    public Optional<UserResponseDto> findUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toDto);
    }

    @Override
    public UserResponseDto saveUser(UserRequestDto objUserRequestDto) {

        // 1. DTO -> Entity
        var userEntity = userMapper.toEntity(objUserRequestDto);

        // 2. Guardar Entity
        var savedUser = userRepository.save(userEntity);

        // 3. Entity -> ResponseDTO
        return userMapper.toDto(userEntity);

    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto objUserRequestDto) {

        return userRepository.findById(id).map(existingUser->{
            existingUser.setFirstName(objUserRequestDto.getFirstName());
            existingUser.setLastName(objUserRequestDto.getLastName());
            existingUser.setEmail(objUserRequestDto.getEmail());

            var updated= userRepository.save(existingUser);
            return userMapper.toDto(updated);
        }).orElseThrow(()-> new RuntimeException("User not found"));
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}