package py.una.pol.auth.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import py.una.pol.auth.dto.RoleDto;
import py.una.pol.auth.dto.UserDto;
import py.una.pol.auth.model.User;
import py.una.pol.auth.services.UserService;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuarios del sistema", description = "Controlador que maneja la creación, obtención, actualización y eliminación de usuarios.")
public class UserController {
    private final UserService userService;
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /* registrar un nuevo usuario
     * @param: userDto: los datos ingresados del usuario a crear
     * @return: el objeto User que representa al usuario creado
     */

    @PostMapping("/register")
    /* Anotaciones para swagger */
    @Operation(summary="Registra un nuevo usuario")
    @ApiResponses(value={
    @ApiResponse(responseCode = "500",description="Error del lado del servidor al registrar el usuario solicitado"),
    @ApiResponse(responseCode = "201",description="Usuario Registrado Exitosamente"),
    @ApiResponse(responseCode = "400",description="Datos no validos en la solicitud de registro de usuario")
    })

     public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto){
        try{
            User registerUser=userService.createUser(userDto);

            UserDto userData=convertToDto(registerUser);

            return new ResponseEntity<>(userData,HttpStatus.CREATED);
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    /* Metodo que obtiene todos los usuarios del sistema
     * @return Lista de usuarios del sistema
     */
    @GetMapping
    @Operation(summary = "Metodo que obtiene todos los usuarios", description = "Retorna una lista de todos los usuarios registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente."),
            @ApiResponse(responseCode = "500", description = "Error en el servidor al obtener los usuarios.")
    })
    
    public ResponseEntity<List<UserDto>> getAllUsers(){
        try {
            List<User> users=userService.getAllUsers();
            List<UserDto> usersDto=users.stream().map(this::convertToDto).collect(Collectors.toList());

            return ResponseEntity.ok(usersDto);
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    /* Metodo que asigna un rol a un usuario 
     * @param: id del usuario a asignar los roles
     * @param: lista de roles a ser asignados
    */
    @PutMapping("/{userId}/roles")
    @Operation(summary="Metodo que asigna un rol a un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Roles asignados exitosamente."),
        @ApiResponse(responseCode = "404", description = "Usuario o roles no encontrados."),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida."),
        @ApiResponse(responseCode = "500", description = "Error en el servidor.")
    })

    public ResponseEntity<String> assignRoles(@PathVariable Long userId, @RequestBody List<Long> rolesId){
        try{
            userService.assignRoles(userId,rolesId);

            return ResponseEntity.ok("Roles asignados con éxito");
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        // Agregar roles si es necesario
        return dto;
    }

   /* Obtiene los roles asignados a un usuario */
    @GetMapping("/{userId}/roles")
    @Operation(summary = "Obtiene los roles de un usuario", description = "Devuelve una lista de roles asignados a un usuario específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de roles obtenida exitosamente."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado."),
            @ApiResponse(responseCode = "500", description = "Error en el servidor al obtener los roles del usuario.")
    })
    public ResponseEntity<List<RoleDto>> getRolesByUserId(@PathVariable Long userId) {
        try {
            List<RoleDto> roles = userService.getRolesByUserId(userId);
            return ResponseEntity.ok(roles);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}