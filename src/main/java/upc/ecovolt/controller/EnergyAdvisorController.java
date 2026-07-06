package upc.ecovolt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upc.ecovolt.mapping.dto.ApiResponseDto;
import upc.ecovolt.mapping.dto.EnergyAdvisorDto;
import upc.ecovolt.service.EnergyAdvisorService;
import upc.ecovolt.util.AppSettings;
import upc.ecovolt.util.WebUtil;

@Tag(name = "Energy Advisor", description = "Recomendaciones inteligentes de consumo energetico")
@RestController
@RequestMapping("/api/v1/advisor")
@RequiredArgsConstructor
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class EnergyAdvisorController {

    private final EnergyAdvisorService advisorService;

    @PostMapping("/analyze")
    @Operation(summary = "Analizar consumo con IA", description = "Calcula metricas reales y genera recomendaciones con Gemini o reglas locales.")
    public ResponseEntity<ApiResponseDto<EnergyAdvisorDto.Response>> analyze(@RequestBody EnergyAdvisorDto.Request request) {
        var data = advisorService.analyze(request);
        String source = Boolean.TRUE.equals(data.getAiGenerated()) ? "Gemini" : "reglas locales";
        return WebUtil.ok(data, "Analisis energetico generado con " + source + ".");
    }
}
