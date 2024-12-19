package py.una.pol.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
/* Data Transfer Object que representa a un usuario del sistema */
@Data
public class UserDto {

    private long id; 
    @Schema(description = "Nombre de Usuario", example = "user123")
    private String username;

    @Schema(description = "Contraseña del Usuario", example = "password1234",hidden = true)
    private String password;
}
