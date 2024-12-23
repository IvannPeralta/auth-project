package py.una.pol.auth.services;

import org.springframework.stereotype.Service;

import py.una.pol.auth.repository.RoleRepository;
import py.una.pol.auth.repository.UserRepository;
import py.una.pol.auth.model.Role;
import py.una.pol.auth.model.User;
import py.una.pol.auth.dto.RoleDto;
import py.una.pol.auth.dto.RoleDto;
import py.una.pol.auth.dto.UserDto;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

/* Servicio que maneja los  usuarios en el sistema
 * Posee metodos para crear usuarios, obtener, actualizar y eliminar
*/
@Service
public class UserService {
    private final RoleRepository role;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    /* Constructor del servicio utilizado para pasar los datos a los repositorios de usuarios, codificador de contraseñas y roles de usuario */

    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder, RoleRepository role){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.role=role;
    }

    /* Servicio que crea un usuario a partir de los datos proporcionados 
     * @param userDto: el objeto que contiene la informacion del usuario
     * @return: un objeto User que representa al usuario creado
     * @throws: IlegalArgumentException: si el campo username esta vacio
    */

    public User createUser(UserDto userDto){
        if(userDto.getUsername()==null || userDto.getUsername().isEmpty()){
            throw new IllegalArgumentException("El campo de nombre de usuario no puede ser vacio");
        }

        if(userRepository.findByUsername(userDto.getUsername())!=null){
            throw new IllegalArgumentException("Este nombre de usuario ya existe.");
        }


        User newUser= new User();

        /* asignar el username y password contenido en el objeto userDto a la instancia newUser, que representa al usuario nue */
        newUser.setUsername(userDto.getUsername());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        
        /* Guardar el usuario */
        try{
            return userRepository.save(newUser);
        }catch (DataIntegrityViolationException e){
            throw new RuntimeException("Error al guardar el usuario: " + e.getMessage());
        }
    }

    /* Metodo para obtener un usuario por su nombre de usuario 
     * @pararm username: El nombre de usuario a buscar
     * @return: objeto User que representa el usuario encontrado
    */

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    /* Metodo que obtiene todos los usuarios */
    public List<User> getAllUsers(){
        return userRepository.findAll();    
    }

    /* Metodo que obtiene un usuario por su id */
    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }

    /* Metodo que obtiene todos los roles de un usuario por su id}@param: id del usuario
     * @return: lista de los roles del usuario
     */
    public List<RoleDto> getRolesByUserId(Long userId) {
        List<Role> roles = userRepository.findRolesByUserId(userId);
        return roles.stream()
                .map(role -> {
                    RoleDto roleDTO = new RoleDto();
                    roleDTO.setId(role.getId());
                    roleDTO.setName(role.getName());
                    return roleDTO;
                })
                .collect(Collectors.toList());
    }

    /*
     *Metodo asociado al Controller que actualiza la informacion de un usuario
     *
     * @param id El ID del usuario a actualizar.
     * @param userDto El UserDTO que contiene la nueva información del usuario.
     * @return User que representa el usuario actualizado.
     * @throws RuntimeException Si no se encuentra el usuario.
     */
    public User updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")); // Manejo de errores
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword())); // Codifica la nueva contraseña
        return userRepository.save(user);
    }

    /**
     * Elimina un usuario por su ID.
     *
     * @param id El ID del usuario a eliminar.
     */
    public boolean deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            return false; // Usuario no encontrado
        }
        userRepository.deleteById(id);
        return true; // Eliminación exitosa
    }

    /* Metodo que asigna roles a un usuario
     * @param id de usuario
     * @param Lista de roles a ser asignados a dicho usuario
     */
    public void assignRoles(Long userId,List<Long> rolesId){
        User user=userRepository.findById(userId).orElseThrow(()-> new RuntimeException("Usuario no encontrado"));

        List<Role> roles=role.findAllById(rolesId);

        if(roles.size()!=rolesId.size()){
            throw new RuntimeException("Los roles seleccionados no fueron encontrados");
        }

        user.setRoles(new HashSet<>(roles));

        userRepository.save(user);
    }

    public List<RoleDto> getRolesByUserId(Long userId) {
        List<Role> roles = userRepository.findRolesByUserId(userId);
        return roles.stream()
                .map(role -> {
                    RoleDto roleDTO = new RoleDto();
                    roleDTO.setId(role.getId());
                    roleDTO.setName(role.getName());
                    return roleDTO;
                })
                .collect(Collectors.toList());
    }

    /**
     * Asigna múltiples roles a un usuario. (Versión básica, sin quitar roles existentes).
     *
     * @param userId  ID del usuario.
     * @param roleIds IDs de los roles a asignar.
     */
    public void assignRolesToUser(Long userId, List<Long> roleIds) {
        // Verificar que el usuario existe
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    
        System.out.println("Usuario encontrado: " + user.getUsername());
        System.out.println("Roles actuales del usuario: " + user.getRoles().stream()
                .map(Role::getName).collect(Collectors.toList()));
    
        // Verificar que todos los roles existen
        List<Role> roles = role.findAllById(roleIds);
    
        // Si no se encuentran todos los roles, lanzar una excepción
        if (roles.size() != roleIds.size()) {
            throw new RuntimeException("Some roles not found");
        }
    
        // Sobrescribir roles del usuario
        user.setRoles(new HashSet<>(roles)); // Usamos un HashSet para evitar duplicados
    
        // Guardar el usuario con los roles actualizados
        userRepository.save(user);

    }

    /**
     * Asigna múltiples roles a un usuario. (Versión básica, sin quitar roles existentes).
     *
     * @param userId  ID del usuario.
     * @param roleIds IDs de los roles a asignar.
     */
    public void assignRolesToUser(Long userId, List<Long> roleIds) {
        // Verificar que el usuario existe
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    
        System.out.println("Usuario encontrado: " + user.getUsername());
        System.out.println("Roles actuales del usuario: " + user.getRoles().stream()
                .map(Role::getName).collect(Collectors.toList()));
    
        // Verificar que todos los roles existen
        List<Role> roles = role.findAllById(roleIds);
    
        // Si no se encuentran todos los roles, lanzar una excepción
        if (roles.size() != roleIds.size()) {
            throw new RuntimeException("Some roles not found");
        }
    
        // Sobrescribir roles del usuario
        user.setRoles(new HashSet<>(roles)); // Usamos un HashSet para evitar duplicados
    
        // Guardar el usuario con los roles actualizados
        userRepository.save(user);
    
        System.out.println("Roles finales del usuario: " + user.getRoles().stream()
                .map(Role::getName).collect(Collectors.toList()));
    }

   
    
}
