package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.entity.Role;
import upc.ecovolt.entity.User;
import upc.ecovolt.entity.Option;
import upc.ecovolt.mapping.dto.userdto.UserMapper;
import upc.ecovolt.mapping.dto.userdto.UserRequestDto;
import upc.ecovolt.mapping.dto.userdto.UserResponseDto;
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
    public List<UserResponseDto> findAllUsers() {
        return userMapper.toDtoList(userRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponseDto> findUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toDto);
    }

    @Override
    @Transactional
    public UserResponseDto saveUser(UserRequestDto requestDto) {
        log.info("Registrando nuevo usuario con login: {}", requestDto.getLogin());

        // 1. Validar unicidad (Regla de Negocio)
        if (userRepository.findByLogin(requestDto.getLogin()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya existe.");
        }
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado.");
        }

        // 2. Mapear DTO a Entidad
        User user = userMapper.toEntity(requestDto);

        // 3. SEGURIDAD: Encriptar password antes de guardar
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        // 4. SaaS: Asignar Plan de Suscripción real
        var plan = planRepository.findById(requestDto.getSubscriptionPlanId())
                .orElseThrow(() -> new RuntimeException("Plan no encontrado"));
        user.setSubscriptionPlan(plan);

        // 5. SEGURIDAD: Asignar Rol por defecto (ROLE_CUSTOMER)
        Role defaultRole = roleRepository.findByNombre("ROLE_CUSTOMER")
                .orElseThrow(() -> new RuntimeException("Error: El Rol por defecto no existe en la BD"));
        user.setRoles(Set.of(defaultRole));

        // 6. Auditoría inicial
        user.setUsuarioRegistro("SELF_REGISTER");

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long id, UserRequestDto requestDto) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setFirstName(requestDto.getFirstName());
            existingUser.setLastName(requestDto.getLastName());
            existingUser.setEmail(requestDto.getEmail());

            // Actualizar plan si es necesario
            var plan = planRepository.findById(requestDto.getSubscriptionPlanId())
                    .orElseThrow(() -> new RuntimeException("Plan no encontrado"));
            existingUser.setSubscriptionPlan(plan);

            return userMapper.toDto(userRepository.save(existingUser));
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
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
    public Optional<UserResponseDto> findByLogin(String login) {
        return userRepository.findByLogin(login).map(userMapper::toDto);
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