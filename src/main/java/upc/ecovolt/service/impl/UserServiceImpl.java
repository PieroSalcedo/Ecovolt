package upc.ecovolt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.ecovolt.entity.SubscriptionPlan;
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
    @Transactional
    public UserDto.Response saveUser(UserDto.Request requestDto) {
        // ... validaciones de login existente ...

        User user = userMapper.toEntity(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        // LÓGICA DE PLAN: Si viene un ID de plan, usamos ese. Si no, el ID 1.
        Integer planId = (requestDto.getIdPlan() != null) ? requestDto.getIdPlan() : 1;

        SubscriptionPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan no válido"));
        user.setSubscriptionPlan(plan);

        // Rol por defecto
        Role role = new Role(); role.setIdRole(1);
        user.setRoles(Set.of(role));
        user.setStatus(1);

        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void updateUserPlan(Long idUser, Integer idPlan) {
        log.info("Actualizando plan del usuario ID: {} al Plan ID: {}", idUser, idPlan);
        userRepository.updatePlan(idUser, idPlan);
    }

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
    public UserDto.Response updateUser(Long idUser, UserDto.Request requestDto) {
        return userRepository.findById(idUser).map(existing -> {
            existing.setEmail(requestDto.getEmail());
            existing.setFirstName(requestDto.getFirstName());
            existing.setLastName(requestDto.getLastName());
            return userMapper.toResponseDto(userRepository.save(existing));
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    @Transactional
    public void delete(Long idUser) {
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
        return userRepository.getDeviceLimitByUserId(idUser).orElse(0);
    }

    @Override
    @Transactional(readOnly = true)
    public long countUsersByCity(String city) {
        return userRepository.countUsersByCity(city);
    }
}