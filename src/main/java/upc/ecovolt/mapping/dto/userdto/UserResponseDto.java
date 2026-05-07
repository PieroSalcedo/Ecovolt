package upc.ecovolt.mapping.dto.userdto;

import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Long subscriptionPlanId;
    private Integer status;
}