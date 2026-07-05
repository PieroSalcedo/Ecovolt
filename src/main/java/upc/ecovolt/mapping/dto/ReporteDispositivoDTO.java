package upc.ecovolt.mapping.dto;
import lombok.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReporteDispositivoDTO {
    private String dispositivo;
    private BigDecimal consumo;
}