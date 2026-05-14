package upc.ecovolt.mapping.dto.roledto;

import lombok.Data;

@Data
public class RoleResponseDto {
    private Integer idRol;
    private String nombre;
    private Integer estado; // 1: Activo, 0: Inactivo
}