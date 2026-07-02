package upc.ecovolt.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import upc.ecovolt.entity.Home;
import upc.ecovolt.entity.User;
import upc.ecovolt.mapping.dto.ApiResponseDto;
import upc.ecovolt.mapping.dto.HomeDto;
import upc.ecovolt.security.UsuarioPrincipal;
import upc.ecovolt.service.HomeService;
import upc.ecovolt.util.AppSettings;
import upc.ecovolt.util.WebUtil;

import java.util.List;

@Tag(name = "Homes", description = "Gestión de propiedades")
@RestController
@RequestMapping("/api/v1/homes")
@RequiredArgsConstructor
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
@Slf4j
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/consultaDinamica")
    public ResponseEntity<ApiResponseDto<List<HomeDto.Response>>> consulta(
            @RequestParam(defaultValue = "") String alias,
            @RequestParam(defaultValue = "") String city,
            @RequestParam(defaultValue = "-1") int idTipo) {

        UsuarioPrincipal principal = (UsuarioPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Llamamos al service que ahora devuelve DTOs
        List<HomeDto.Response> listaDto = homeService.consultaHomeDinamica(principal.getIdUser(), alias, city, idTipo);

        return WebUtil.ok(listaDto, "Viviendas cargadas con éxito");
    }

    // --- REGISTRO ---
    @PostMapping("/registra")
    @Operation(summary = "Registrar vivienda", description = "Si es Customer, se ignora el idUser y se asigna el suyo.")
    public ResponseEntity<ApiResponseDto<HomeDto.Response>> save(@RequestBody HomeDto.Request request) {
        var principal = (UsuarioPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // LÓGICA DE NEGOCIO: Seguridad de asignación
        if (!isAdmin) {
            request.setIdUser(principal.getIdUser());
        }

        var data = homeService.saveHome(request);
        return WebUtil.created(data, "Vivienda '" + data.getAlias() + "' registrada con éxito.");
    }

    // --- ACTUALIZACIÓN ---
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos de vivienda")
    public ResponseEntity<ApiResponseDto<HomeDto.Response>> update(@PathVariable Long id, @RequestBody HomeDto.Request request) {
        var data = homeService.updateHome(id, request);
        return WebUtil.ok(data, "Datos de la propiedad actualizados.");
    }

    // --- ELIMINACIÓN ---
    @DeleteMapping("/elimina/{id}") // Asegúrate de que diga "/elimina/{id}"
    public ResponseEntity<ApiResponseDto<Void>> delete(@PathVariable Long id) {
        log.info("Eliminando vivienda con ID: {}", id);
        homeService.delete(id);
        return WebUtil.ok(null, "Vivienda eliminada correctamente");
    }

    // --- CONSULTAS ---

    @GetMapping("/my-list")
    @Operation(summary = "Listar MIS casas", description = "Endpoint principal para el Dashboard del usuario logueado.")
    public ResponseEntity<ApiResponseDto<List<HomeDto.Response>>> getMyHomes() {
        var principal = (UsuarioPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var data = homeService.findActiveHomesByUser(principal.getIdUser());
        return WebUtil.ok(data, "Lista de propiedades cargada.");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle de vivienda")
    public ResponseEntity<ApiResponseDto<HomeDto.Response>> getById(@PathVariable Long id) {
        return homeService.findHomeById(id)
                .map(home -> WebUtil.ok(home, "Detalle de la casa"))
                .orElseThrow(() -> new RuntimeException("No se encontró la propiedad"));
    }

    @GetMapping("/{id}/device-count")
    @Operation(summary = "Total de dispositivos", description = "Muestra cuántos equipos IoT hay en toda la casa.")
    public ResponseEntity<ApiResponseDto<Long>> getDeviceCount(@PathVariable Long id) {
        var count = homeService.countTotalDevicesByHome(id);
        return WebUtil.ok(count, "Conteo de inventario completado.");
    }

    // Solo para el ADMIN: Listar todas las casas del sistema
    @GetMapping("/admin/all")
    @Operation(summary = "Listar todas (ADMIN)")
    public ResponseEntity<ApiResponseDto<List<HomeDto.Response>>> getAllAdmin() {
        var data = homeService.findAllHomes();
        return WebUtil.ok(data, "Inventario global de viviendas.");
    }
}