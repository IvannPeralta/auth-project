package py.una.pol.auth.controllers;

import py.una.pol.auth.services.RoleService;

import java.util.List;
import py.una.pol.auth.dto.PermissionDto;
import py.una.pol.auth.dto.RoleDto;
import py.una.pol.auth.dto.UserDto;
import py.una.pol.auth.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Roles del sistema", description = "Controlador para manejar la creación, obtención, actualización y eliminación de roles.")
public class RoleController {
    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /* Metodo para obtener todos los roles del sistema */
    @GetMapping
    @Operation(summary = "Metodo que obtiene todos los roles del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de roles del sistema obtenida exitosamente."),
            @ApiResponse(responseCode = "500", description = "Error en el servidor al obtener los roles del sistema.")
        })

    public ResponseEntity<List<RoleDto>> getAllRoles(){
        try{
            return new ResponseEntity<>(roleService.findAll(),HttpStatus.OK);
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /* Metodo que crea un nuevo rol del sistema 
     * @param roleDto: el rol a crear
     * return: el objeto RoleDto representando al rol creado
    */

    @PostMapping
    @Operation(summary = "Crear un nuevo rol", description = "Metodo que crea un nuevo rol en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rol creado exitosamente."),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida."),
            @ApiResponse(responseCode = "500", description = "Error en el servidor al crear el rol.")
    })

    public ResponseEntity<RoleDto> createRol(@RequestBody RoleDto roleData){
        try{
            RoleDto createdRole=roleService.createRol(roleData);

            return new ResponseEntity<>(createdRole,HttpStatus.CREATED);
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /* Metodo que asigna permisos a un rol del sistema */
    @PostMapping("/{roleId}/permissions")
    @Operation(summary = "Asignar permisos a un rol", description = "Asignar permisos a un rol en especifico del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Permisos asignados exitosamente."),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado."),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida."),
            @ApiResponse(responseCode = "500", description = "Error en el servidor al asignar permisos.")
    })
    public ResponseEntity<Void> assignPermissions(@PathVariable Long roleId, @RequestBody List<Long> permissionIds) {
        roleService.assignPermissionstoRol(roleId, permissionIds);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
