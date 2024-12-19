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

    /* metodo que obtiene todos los permisos del sistema */
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
     * @param: recibe un pbjeto PermissionDto como parametro y retorna un objeto PermissionDto que representa el permiso creado
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


}
