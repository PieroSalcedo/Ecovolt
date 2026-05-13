package upc.ecovolt.mapping.dto.roledto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleResponseDto {

    private Long id;
    private String roleName;
    private String description;
    private Integer status;
}