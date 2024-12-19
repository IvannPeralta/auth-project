package py.una.pol.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/* Data Transfer Object que representa a un Permiso del sistema */
@Data
public class PermissionDto {

    @Schema(description = "ID único del permiso", example = "3")
    private Long id; // ID del permiso

    @Schema(description = "Nombre del permiso", example = "PERMISSION_CREATE_ROLE")
    private String name; // Nombre del permiso

    @Schema(description = "Descripción del permiso", example = "Permite editar roles del sistema")
    private String description; // Descripción del permiso
}