package upc.ecovolt.mapping.dto.userroledto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleResponseDto {

    private Long id;
    private Long userId;
    private Long roleId;
    private Integer status;
}