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

    /*
     * Metodo que obtiene un rol por su id
     *
     * @param id El ID del rol.
     * @return El rol correspondiente al ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un rol por ID", description = "Devuelve los detalles de un rol específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol encontrado."),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado."),
            @ApiResponse(responseCode = "500", description = "Error en el servidor al obtener el rol.")
    })
    public ResponseEntity<RoleDto> getRoleById(@PathVariable Long id) {
        RoleDto role = roleService.findById(id);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }


    /**
     * Actualiza un rol existente.
     *
     * @param id El ID del rol a actualizar.
     * @param roleDto El DTO con los nuevos datos del rol.
     * @return El rol actualizado.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un rol existente", description = "Actualiza los detalles de un rol específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol actualizado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado."),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida."),
            @ApiResponse(responseCode = "500", description = "Error en el servidor al actualizar el rol.")
    })
    public ResponseEntity<RoleDto> updateRole(@PathVariable Long id, @RequestBody RoleDto roleDto) {
        RoleDto updatedRole = roleService.updateRole(id, roleDto);
        return new ResponseEntity<>(updatedRole, HttpStatus.OK);
    }

    /**
     * Elimina un rol existente.
     *
     * @param id El ID del rol a eliminar.
     * @return Respuesta sin contenido.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un rol existente", description = "Elimina un rol específico según su ID. Devuelve un código 204 si la eliminación fue exitosa o un código 404 si el rol no fue encontrado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Rol eliminado exitosamente. No hay contenido adicional en la respuesta."),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado. No se pudo realizar la eliminación porque el ID especificado no existe."),
            @ApiResponse(responseCode = "500", description = "Error en el servidor al intentar eliminar el rol. Puede deberse a problemas internos del servidor.")
    })
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        boolean eliminado = roleService.delete(id);
        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene los permisos asignados a un rol.
     *
     * @param roleId El ID del rol.
     * @return Lista de permisos asignados al rol.
     */
    @GetMapping("/{roleId}/permissions")
    @Operation(summary = "Obtiene los permisos de un rol", description = "Devuelve una lista de permisos asignados a un rol específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de permisos obtenida exitosamente."),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado."),
            @ApiResponse(responseCode = "500", description = "Error en el servidor al obtener los permisos del rol.")
    })
    public ResponseEntity<?> getPermissionsByRoleId(@PathVariable Long roleId) {


        List<PermissionDto> permissions = roleService.getPermissionsByRoleId(roleId);
        // Si no se encuentran permisos, lanzar una excepción
        if (permissions.isEmpty()) {
            if (permissions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontraron permisos para el rol especificado.");
            }
        }
        return new ResponseEntity<>(permissions, HttpStatus.OK);
    }
    


    // Método en el controlador para asignar permisos a un rol
    @PutMapping("/{roleId}/permissions")
    @Operation(summary = "Asigna permisos a un rol", description = "Asigna una lista de permisos a un rol específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permisos asignados exitosamente."),
            @ApiResponse(responseCode = "404", description = "Rol o permisos no encontrados."),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida."),
            @ApiResponse(responseCode = "500", description = "Error en el servidor.")
    })
    public ResponseEntity<String> assignPermissionsToRole(@PathVariable Long roleId, @RequestBody List<Long> permissionIds) {
        try {
            roleService.assignPermissionsToRole(roleId, permissionIds);
            return ResponseEntity.ok("Permisos asignados exitosamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
}
