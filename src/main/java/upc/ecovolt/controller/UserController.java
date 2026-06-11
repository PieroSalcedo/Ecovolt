package upc.ecovolt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upc.ecovolt.entity.Option;
import upc.ecovolt.entity.Role;
import upc.ecovolt.mapping.dto.UserDto;
import upc.ecovolt.service.UserService;
import upc.ecovolt.util.AppSettings;

import java.util.List;

@Tag(name = "Users", description = "Endpoints para la gestión de cuentas de usuario, seguridad dinámica y cuotas SaaS")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class UserController {

    private final UserService userService;

    @Operation(summary = "Listar todos los usuarios registrados", description = "Retorna el perfil básico de todos los usuarios del sistema")
    @GetMapping
    public ResponseEntity<List<UserDto.Response>> getAll() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @Operation(summary = "Obtener un usuario por su ID", description = "Busca la información detallada de una cuenta específica")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    @ApiResponse(responseCode = "404", description = "Usuario no existe")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto.Response> getById(
            @Parameter(description = "ID único del usuario", example = "1")
            @PathVariable Long id) {
        return userService.findUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Registrar un nuevo usuario", description = "Crea una cuenta, encripta la clave y asigna el Plan de Suscripción indicado")
    @PostMapping("/register")
    public ResponseEntity<UserDto.Response> register(@Valid @RequestBody UserDto.Request request) {
        return new ResponseEntity<>(userService.saveUser(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar perfil de usuario", description = "Modifica nombres, apellidos, email o el plan de suscripción")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto.Response> update(
            @Parameter(description = "ID del usuario a modificar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UserDto.Request request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @Operation(summary = "Eliminar cuenta de usuario", description = "Realiza la desactivación de la cuenta en el sistema")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del usuario a eliminar", example = "5")
            @PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --- ENDPOINTS DE SEGURIDAD Y REGLAS DE NEGOCIO ---

    @Operation(summary = "Obtener accesos dinámicos del usuario", description = "Lista las rutas y menús permitidos según el rol del usuario (Criterio del Profesor)")
    @GetMapping("/{id}/menu")
    public ResponseEntity<List<Option>> getUserMenu(
            @Parameter(description = "ID del usuario para cargar su interfaz personalizada", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(userService.traerEnlacesDeUsuario(id));
    }

    @Operation(summary = "Listar roles del usuario", description = "Devuelve los perfiles (Authorities) asignados a la cuenta")
    @GetMapping("/{id}/roles")
    public ResponseEntity<List<Role>> getUserRoles(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(userService.traerRolesDeUsuario(id));
    }

    @Operation(summary = "Verificar cuota de dispositivos (SaaS)", description = "Retorna el número máximo de equipos IoT que el usuario puede registrar según su plan")
    @GetMapping("/{id}/quota")
    public ResponseEntity<Integer> getQuota(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(userService.getDeviceLimitByUserId(id));
    }

    @Operation(summary = "Análisis demográfico de usuarios", description = "Cuenta cuántos usuarios activos residen en una ciudad específica (Valor para DEVIDA)")
    @GetMapping("/analytics/city-count")
    public ResponseEntity<Long> countByCity(
            @Parameter(description = "Nombre de la ciudad", example = "Lima")
            @RequestParam String city) {
        return ResponseEntity.ok(userService.countUsersByCity(city));
    }
}
