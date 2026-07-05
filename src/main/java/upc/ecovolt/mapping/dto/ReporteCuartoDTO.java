package upc.ecovolt.mapping.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReporteCuartoDTO {
    private String cuarto;
    private BigDecimal consumo;
}