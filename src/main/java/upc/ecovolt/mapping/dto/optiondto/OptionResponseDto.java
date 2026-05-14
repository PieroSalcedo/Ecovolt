package upc.ecovolt.mapping.dto.optiondto;

import lombok.Data;

@Data
public class OptionResponseDto {
    private Integer idOption;
    private String nombre;
    private String ruta;
    private Integer tipo;
    private Integer estado;
}