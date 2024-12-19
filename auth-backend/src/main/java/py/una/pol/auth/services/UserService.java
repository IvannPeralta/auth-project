package py.una.pol.auth.services;

import org.springframework.stereotype.Service;

import py.una.pol.auth.repository.RoleRepository;
import py.una.pol.auth.repository.UserRepository;
import py.una.pol.auth.model.Role;
import py.una.pol.auth.model.User;
import py.una.pol.auth.dto.UserDto;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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

    public List<User> getAllUsers(){
        return userRepository.findAll();    
    }

    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }



    public void assignRoles(Long userId,List<Long> rolesId){
        User user=userRepository.findById(userId).orElseThrow(()-> new RuntimeException("Usuario no encontrado"));

        List<Role> roles=role.findAllById(rolesId);

        if(roles.size()!=rolesId.size()){
            throw new RuntimeException("Los roles seleccionados no fueron encontrados");
        }

        user.setRoles(new HashSet<>(roles));

        userRepository.save(user);

    }

    
}
