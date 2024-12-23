package py.una.pol.auth.controllers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import py.una.pol.auth.dto.PermissionDto;
import py.una.pol.auth.model.Permission;
import py.una.pol.auth.services.PermissionService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*  */
@RestController
@RequestMapping("/api/permissions")
@Tag(name = "Permisos del sistema", description = "Controlador que maneja la creación, obtención, actualización y eliminación de permisos.")
public class PermissionController {
    private final PermissionService permissionService;

    @Autowired
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    /* metodo que obtiene todos los permisos del sistema 
     * @return: una lista con todos los permisos del sistema
    */
    @GetMapping
    @Operation(summary="Obtener todos los permisos del sistema",description="Metodo que retorna una lista de todos los permisos del sisema")
    @ApiResponses(value={
        @ApiResponse(responseCode="200",description="Se obtuvo exitosamente la lista de permisos"),
        @ApiResponse(responseCode="500",description="Error en el servidor al obtener la lista de permisos"),
    })

    public ResponseEntity<List<PermissionDto>> getAllPermissions(){
        try {
            return new ResponseEntity<>(permissionService.findAll(),HttpStatus.OK);
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /* Metodo que crea un permiso 
     * @param: recibe un objeto PermissionDto como parametro y retorna un objeto PermissionDto que representa el permiso creado
    */

    @PostMapping
    @Operation(summary = "Crea un nuevo permiso en el sistema", description = "EMtodo que crea un nuevo permiso dentro del sistema con los datos del permiso proveidos en la solicitud")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Permiso creado exitosamente."),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida."),
            @ApiResponse(responseCode = "500", description = "Error en el servidor al crear el permiso.")
    })

    public ResponseEntity<PermissionDto> createPermission(@RequestBody PermissionDto permissionData){
        try {
            
            return new ResponseEntity<>(permissionService.create(permissionData),HttpStatus.CREATED);
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Obtiene un permiso por su ID.
     *
     * @param id El ID del permiso.
     * @return El permiso correspondiente al ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un permiso por ID", description = "Devuelve los detalles de un permiso específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permiso encontrado."),
            @ApiResponse(responseCode = "404", description = "Permiso no encontrado."),
            @ApiResponse(responseCode = "500", description = "Error en el servidor al obtener el permiso.")
    })
    public ResponseEntity<PermissionDto> getPermissionById(@PathVariable Long id) {
        PermissionDto permission = permissionService.findById(id);
        return new ResponseEntity<>(permission, HttpStatus.OK);
    }

    /*
     * Actualiza un permiso existente.
     *
     * @param id El id del permiso a actualizar.
     * @param permissionDto El objeto Dto con los nuevos datos del permiso.
     * @return El permiso actualizado.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un permiso existente", description = "Actualiza los detalles de un permiso específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permiso actualizado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Permiso no encontrado."),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida."),
            @ApiResponse(responseCode = "500", description = "Error en el servidor al actualizar el permiso.")
    })
    public ResponseEntity<PermissionDto> updatePermission(@PathVariable Long id, @RequestBody PermissionDto permissionDto) {
        PermissionDto updatedPermission = permissionService.update(id, permissionDto);
        return new ResponseEntity<>(updatedPermission, HttpStatus.OK);
    }

    /*
     * Elimina un permiso existente.
     *
     * @param id El id del permiso a eliminar.
     * @return Respuesta sin contenido.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un permiso existente", description = "Elimina un permiso específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Permiso eliminado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Permiso no encontrado."),
            @ApiResponse(responseCode = "500", description = "Error en el servidor al eliminar el permiso.")
    })
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        permissionService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}
