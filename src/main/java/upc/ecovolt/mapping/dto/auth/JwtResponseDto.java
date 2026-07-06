package upc.ecovolt.mapping.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import upc.ecovolt.mapping.dto.OptionDto;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponseDto {
    private String token;
    private String type = "Bearer";
    private Long idUser;
    private String login;
    private String fullName;
    private List<String> roles;
    private List<OptionDto.Response> opciones;
}