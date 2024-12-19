package py.una.pol.auth.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import py.una.pol.auth.dto.UserDto;
import py.una.pol.auth.model.Role;
import py.una.pol.auth.model.User;
import py.una.pol.auth.services.UserService;
import py.una.pol.auth.util.JwtUtil;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class SessionController {
    private final UserService userActions;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public SessionController(PasswordEncoder passwordEncoder, UserService userActions,JwtUtil jwtUtil){
        this.userActions=userActions;
        this.passwordEncoder=passwordEncoder;
        this.jwtUtil=jwtUtil;
    }


    @PostMapping("/login")
    @Operation(summary="Iniciar Sesión",description = "Se debe ingresar el usuario y contraseña para acceder al sitio")

    public String loginSession(@RequestBody UserDto userDTO){
        User user=userActions.getUserByUsername(userDTO.getUsername());

        /* comprobar si existe el usuario y si la contraseña es correcta */

        if(user!=null && passwordEncoder.matches(userDTO.getPassword(),user.getPassword())){

            /* obtener los roles del usuario */

            Set<String> rolesUsuario=user.getRoles().stream().map(rol->rol.getName()).collect(Collectors.toSet());
            return jwtUtil.generateTokenWithRoles(user.getUsername(),rolesUsuario);
        }
        else{
            throw new BadCredentialsException("Los datos proporcionados son incorrectos.");
        }

    }
}
