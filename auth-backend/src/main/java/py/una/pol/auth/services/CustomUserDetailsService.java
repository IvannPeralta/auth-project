package py.una.pol.auth.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import py.una.pol.auth.repository.UserRepository;
import py.una.pol.auth.model.User;
import py.una.pol.auth.model.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Servicio personalizado para cargar detalles de usuario.
 * Esta clase implementa la interfaz {@link UserDetailsService} para
 * cargar información de usuario desde el repositorio y convertir los roles
 * del usuario en autoridades de seguridad.
 */
@Service
@Transactional                  // para que la sesion de hibernate permanezca activa durante toda la transaccion
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository; // Repositorio para acceder a los datos de usuario

    /**
     * Constructor que inyecta el repositorio de usuarios.
     *
     * @param userRepository El repositorio de usuarios.
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Carga un usuario específico por su nombre de usuario.
     *
     * @param username El nombre de usuario a buscar.
     * @return UserDetails que contiene la información del usuario y sus roles.
     * @throws UsernameNotFoundException Si el usuario no se encuentra en el repositorio.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca al usuario en tu repositorio
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Convertir roles a GrantedAuthority
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName())); // Asegúrate de que Role tenga un método getName()
        }

        // Crea y devuelve un objeto de UserDetails usando tu User
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}
