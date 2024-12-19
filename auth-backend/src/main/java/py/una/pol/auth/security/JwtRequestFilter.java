package py.una.pol.auth.security;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import py.una.pol.auth.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Logger;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = Logger.getLogger(JwtRequestFilter.class.getName());
    private final JwtUtil jwtUtil;

    public JwtRequestFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
        throws ServletException, IOException {

    final String authorizationHeader = request.getHeader("Authorization");

    logger.info("Procesando solicitud: " + request.getMethod() + " " + request.getRequestURI());

    String username = null;
    String jwt = null;

    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        jwt = authorizationHeader.substring(7); // Extraer el token sin 'Bearer '
        logger.info("Token extraído: " + jwt);

        try {
            username = jwtUtil.extractUsername(jwt); // Extraer el nombre de usuario del token
            logger.info("Usuario extraído del token: " + username);
        } catch (Exception e) {
            logger.warning("Error al extraer el usuario del token: " + e.getMessage());
        }
    } else {
        logger.warning("Encabezado Authorization no válido o no presente.");
    }

    if (username != null && jwtUtil.validateToken(jwt, username)) {
        logger.info("Token válido para el usuario: " + username);

        // Extraer roles desde el token
        Set<String> roles = jwtUtil.extractRoles(jwt); // Extraemos los roles del token
        logger.info("Roles extraídos del token: " + roles);

        // Mapear roles a GrantedAuthority
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new) // Convertir cada rol a SimpleGrantedAuthority
                .toList();

        // Establecer autenticación en el contexto de seguridad
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(username, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        logger.info("Autenticación establecida para el usuario: " + username + " con roles: " + roles);
    } else {
        logger.warning("Token inválido o usuario no encontrado.");
    }

    // Continuar con la cadena de filtros
    chain.doFilter(request, response);
}

}
