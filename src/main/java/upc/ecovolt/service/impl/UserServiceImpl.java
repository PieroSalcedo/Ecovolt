package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.entity.User;
import upc.ecovolt.entity.Role;
import upc.ecovolt.mapping.dto.*;
import upc.ecovolt.repository.RoleRepository;
import upc.ecovolt.repository.SubscriptionPlanRepository;
import upc.ecovolt.repository.UserRepository;
import upc.ecovolt.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SubscriptionPlanRepository planRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final OptionMapper optionMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto.Response> findAllUsers() {
        return userMapper.toResponseDtoList(userRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto.Response> findUserById(Long idUser) {
        return userRepository.findById(idUser).map(userMapper::toResponseDto);
    }

    @Override
    @Transactional
    public UserDto.Response saveUser(UserDto.Request requestDto) {
        log.info("REGISTRO: Creando cuenta para login: {}", requestDto.getName()); // Usando name como login según tu DTO

        // 1. VALIDACIÓN: Disponibilidad de Login y Email
        if (userRepository.existsByLogin(requestDto.getName())) {
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }

        // 2. MAPEO Y ENCRIPTACIÓN
        User user = userMapper.toEntity(requestDto);
        user.setLogin(requestDto.getName()); // Sincronización con el campo login de la entidad
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        // 3. REGLA DE NEGOCIO SaaS: Registro por defecto (Plan Free + ROLE_CUSTOMER)
        var freePlan = planRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Error: Plan base (Free) no encontrado."));
        user.setSubscriptionPlan(freePlan);

        Role customerRole = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseThrow(() -> new RuntimeException("Error: Rol base no configurado."));
        user.setRoles(Set.of(customerRole));

        user.setStatus(1); // Activo por defecto

        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDto.Response updateUser(Long idUser, UserDto.Request requestDto) {
        return userRepository.findById(idUser).map(existing -> {
            existing.setEmail(requestDto.getEmail());
            // Si el DTO tuviera firstName/lastName, se actualizarían aquí

            return userMapper.toResponseDto(userRepository.save(existing));
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    @Transactional
    public void delete(Long idUser) {
        // REGLA DE NEGOCIO: Borrado lógico para preservar la integridad de toda la cuenta
        userRepository.findById(idUser).ifPresent(u -> {
            u.setStatus(0);
            userRepository.save(u);
            log.warn("BORRADO LÓGICO: Usuario ID {} desactivado", idUser);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto.Response> findByLogin(String login) {
        return userRepository.findByLogin(login).map(userMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OptionDto.Response> findNavOptionsByUserId(Long idUser) {
        // Esta es la consulta clave para que el Frontend dibuje el menú
        var options = userRepository.findNavOptionsByUserId(idUser);
        return optionMapper.toResponseDtoList(options);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDto.Response> findRolesByUserId(Long idUser) {
        var roles = userRepository.findRolesByUserId(idUser);
        return roleMapper.toResponseDtoList(roles);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getDeviceLimitByUserId(Long idUser) {
        return userRepository.getDeviceLimitByUserId(idUser)
                .orElse(0);
    }

    @Override
    @Transactional(readOnly = true)
    public long countUsersByCity(String city) {
        return userRepository.countUsersByCity(city);
    }
}