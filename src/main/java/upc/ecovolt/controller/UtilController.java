package upc.ecovolt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import upc.ecovolt.mapping.dto.*;
import upc.ecovolt.security.UsuarioPrincipal;
import upc.ecovolt.service.*;
import upc.ecovolt.util.WebUtil;
import upc.ecovolt.util.AppSettings;

import java.util.List;

@Tag(name = "Utilities", description = "Endpoints para cargar selectores y diccionarios (Comboboxes)")
@RestController
@RequestMapping("/api/v1/utils")
@RequiredArgsConstructor
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class UtilController {

    private final DataCatalogService dataCatalogService;
    private final SubscriptionPlanService planService;
    private final UserService userService;
    private final HomeService homeService;
    private final RoomService roomService;

    @Operation(summary = "Combo de Diccionarios", description = "Carga 'ROOM_TYPES', 'DEVICE_CATEGORIES', 'PROPERTY_TYPES', etc.")
    @GetMapping("/catalog/{description}")
    public ResponseEntity<ApiResponseDto<List<DataCatalogDto.Response>>> getCatalog(@PathVariable String description) {
        var data = dataCatalogService.findByCatalogDescription(description);
        return WebUtil.ok(data, "Opciones de '" + description + "' cargadas.");
    }

    @Operation(summary = "Combo de Planes", description = "Llenar el selector de planes en el registro de usuarios.")
    @GetMapping("/plans")
    public ResponseEntity<ApiResponseDto<List<SubscriptionPlanDto.Response>>> getPlans() {
        var data = planService.findAllPlans();
        return WebUtil.ok(data, "Catálogo de planes cargado.");
    }

    @Operation(summary = "Combo de Usuarios (ADMIN)", description = "Permite al Admin elegir un dueño al registrar una casa.")
    @GetMapping("/admin/users")
    public ResponseEntity<ApiResponseDto<List<UserDto.Response>>> getAllUsers() {
        var data = userService.findAllUsers();
        return WebUtil.ok(data, "Lista global de usuarios.");
    }

    @Operation(summary = "Combo de Mis Casas", description = "Llenar selector de casas del usuario logueado para registrar un cuarto.")
    @GetMapping("/my-homes")
    public ResponseEntity<ApiResponseDto<List<HomeDto.Response>>> getMyHomes() {
        var principal = (UsuarioPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var data = homeService.findActiveHomesByUser(principal.getIdUser());
        return WebUtil.ok(data, "Tus propiedades han sido cargadas.");
    }

    @Operation(summary = "Combo de Cuartos", description = "Llenar selector de ambientes de una casa elegida para registrar un equipo.")
    @GetMapping("/home/{homeId}/rooms")
    public ResponseEntity<ApiResponseDto<List<RoomDto.Response>>> getRoomsByHome(@PathVariable Long homeId) {
        var data = roomService.findByHomeId(homeId);
        return WebUtil.ok(data, "Ambientes de la propiedad cargados.");
    }
}