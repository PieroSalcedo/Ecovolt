package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.entity.Role;
import upc.ecovolt.entity.User;
import upc.ecovolt.entity.Option;
import upc.ecovolt.mapping.dto.UserDto;
import upc.ecovolt.mapping.dto.UserMapper;
import upc.ecovolt.repository.RoleRepository;
import upc.ecovolt.repository.SubscriptionPlanRepository;
import upc.ecovolt.repository.UserRepository;
import upc.ecovolt.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SubscriptionPlanRepository planRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder; // Inyectado desde SecurityConfig

    @Override
    @Transactional(readOnly = true)
    public List<UserDto.Response> findAllUsers() {
        return userMapper.toResponseDtoList(userRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto.Response> findUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toResponseDto);
    }

    @Override
    @Transactional
    public UserDto.Response saveUser(UserDto.Request requestDto) {
        log.info("REGISTRO: Nuevo prospecto de cliente con login: {}", requestDto.getLogin());

        if (userRepository.findByLogin(requestDto.getLogin()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }

        User user = userMapper.toEntity(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        // REGLA DE NEGOCIO: Todo registro web SIEMPRE es Customer con Plan Free (ID 1)
        var freePlan = planRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Error interno: Plan base no configurado"));
        user.setSubscriptionPlan(freePlan);

        Role customerRole = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseThrow(() -> new RuntimeException("Error interno: Rol base no configurado"));
        user.setRoles(Set.of(customerRole));

        user.setCreatedBy("SELF_SERVICE");

        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDto.Response updateUser(Long id, UserDto.Request requestDto) {
        return userRepository.findById(id).map(existingUser -> {
            // Un usuario normal no puede cambiarse a sí mismo el plan por DTO (SaaS Protection)
            // Solo si es ADMIN se permite cambiar el plan de suscripción

            existingUser.setFirstName(requestDto.getFirstName());
            existingUser.setLastName(requestDto.getLastName());
            existingUser.setEmail(requestDto.getEmail());

            // Auditoría
            existingUser.setUpdatedBy(requestDto.getLogin());

            return userMapper.toResponseDto(userRepository.save(existingUser));
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!userRepository.existsById(id)) throw new RuntimeException("Usuario no existe");
        userRepository.deleteById(id);
    }

    // --- IMPLEMENTACIÓN DE MÉTODOS DE NEGOCIO Y SEGURIDAD ---

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto.Response> findByLogin(String login) {
        return userRepository.findByLogin(login).map(userMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Option> traerEnlacesDeUsuario(Long idUser) {
        return userRepository.traerEnlacesDeUsuario(idUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> traerRolesDeUsuario(Long idUser) {
        return userRepository.traerRolesDeUsuario(idUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getDeviceLimitByUserId(Long idUser) {
        return userRepository.getDeviceLimitByUserId(idUser);
    }

    @Override
    @Transactional(readOnly = true)
    public long countUsersByCity(String city) {
        return userRepository.countUsersByCity(city);
    }
}
