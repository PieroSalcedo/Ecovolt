package upc.ecovolt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import upc.ecovolt.mapping.dto.ApiResponseDto;
import upc.ecovolt.security.UsuarioPrincipal;
import upc.ecovolt.service.UserService;
import upc.ecovolt.util.WebUtil;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @PutMapping("/upgrade-plan/{idPlan}")
    public ResponseEntity<ApiResponseDto<Void>> upgradePlan(@PathVariable Integer idPlan) {
        // Obtenemos el usuario de la sesión (Token JWT)
        UsuarioPrincipal principal = (UsuarioPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        userService.updateUserPlan(principal.getIdUser(), idPlan);

        return WebUtil.ok(null, "¡Felicidades! Tu plan se ha actualizado correctamente.");
    }
}