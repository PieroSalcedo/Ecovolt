package upc.ecovolt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import upc.ecovolt.mapping.dto.ApiResponseDto;
import upc.ecovolt.mapping.dto.UserDto;
import upc.ecovolt.mapping.dto.OptionDto;
import upc.ecovolt.mapping.dto.auth.JwtResponseDto;
import upc.ecovolt.mapping.dto.auth.LoginRequestDto;
import upc.ecovolt.security.JwtProvider;
import upc.ecovolt.security.UsuarioPrincipal;
import upc.ecovolt.service.UserService;
import upc.ecovolt.util.AppSettings;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Authentication", description = "Endpoints para acceso y registro al ecosistema Ecovolt")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserService userService;

    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario y devuelve el Token JWT.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<JwtResponseDto>> login(@Valid @RequestBody LoginRequestDto loginDto) {
        log.info("LOGIN: Intento de acceso para '{}'", loginDto.getLogin());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getLogin(), loginDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);

        UsuarioPrincipal principal = (UsuarioPrincipal) authentication.getPrincipal();
        List<String> roles = principal.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.toList());

        List<OptionDto.Response> opciones = userService.findNavOptionsByUserId(principal.getIdUser());

        JwtResponseDto jwtData = new JwtResponseDto(
                jwt, "Bearer", principal.getIdUser(), principal.getLogin(), principal.getFullName(), roles, opciones
        );

        return ResponseEntity.ok(ApiResponseDto.<JwtResponseDto>builder()
                .title("¡Inicio de Sesión Exitoso!")
                .message("Bienvenido(a) " + principal.getFullName() + ". Acceso concedido.")
                .status("SUCCESS")
                .data(jwtData)
                .build());
    }

    @Operation(summary = "Registrar nuevo usuario", description = "Crea una cuenta de cliente con plan básico por defecto.")
    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<UserDto.Response>> register(@Valid @RequestBody UserDto.Request requestDto) {
        log.info("REGISTRO: Creando nueva cuenta para '{}'", requestDto.getName());

        var newUser = userService.saveUser(requestDto);

        return new ResponseEntity<>(ApiResponseDto.<UserDto.Response>builder()
                .title("¡Cuenta Creada!")
                .message("Hola " + newUser.getName() + ", tu cuenta ha sido registrada. Ahora puedes iniciar sesión.")
                .status("SUCCESS")
                .data(newUser)
                .build(), HttpStatus.CREATED);
    }
}