package py.una.pol.auth.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/* Data transfer object que repersenta a un Rol del sistema */
@Data
public class RoleDto {

    @Schema(description = "Id único del rol", example = "1")
    private Long id; 

    @Schema(description = "Nombre del rol", example = "ROLE_ADMIN")
    private String name;
}
