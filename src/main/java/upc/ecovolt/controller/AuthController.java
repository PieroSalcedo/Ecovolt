package upc.ecovolt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import upc.ecovolt.mapping.dto.ApiResponseDto; // Importante
import upc.ecovolt.mapping.dto.auth.JwtResponseDto;
import upc.ecovolt.mapping.dto.auth.LoginRequestDto;
import upc.ecovolt.security.JwtProvider;
import upc.ecovolt.security.UsuarioPrincipal;
import upc.ecovolt.util.AppSettings;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Authentication", description = "Endpoint para el inicio de sesión y generación de Tokens JWT")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario y devuelve un mensaje de bienvenida con su token.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<JwtResponseDto>> login(@Valid @RequestBody LoginRequestDto loginDto) {
        log.info("Intento de login para el usuario: {}", loginDto.getLogin());

        // 1. Autenticación con Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getLogin(), loginDto.getPassword())
        );

        // 2. Establecer el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Generar el Token JWT
        String jwt = jwtProvider.generateToken(authentication);

        // 4. Obtener datos del usuario logueado
        UsuarioPrincipal principal = (UsuarioPrincipal) authentication.getPrincipal();
        List<String> roles = principal.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.toList());

        // 5. Construir el objeto de datos (Token + Info)
        JwtResponseDto jwtData = new JwtResponseDto(
                jwt,
                "Bearer",
                principal.getIdUser(),
                principal.getLogin(),
                principal.getFullName(),
                roles
        );

        // 6. Retornar la Respuesta Enriquecida con Notificación
        return ResponseEntity.ok(ApiResponseDto.<JwtResponseDto>builder()
                .title("¡Inicio de Sesión Exitoso!")
                .message("Bienvenido(a) " + principal.getFullName() + ". Acceso concedido al ecosistema EcoVolt.")
                .status("SUCCESS")
                .data(jwtData)
                .build());
    }
}