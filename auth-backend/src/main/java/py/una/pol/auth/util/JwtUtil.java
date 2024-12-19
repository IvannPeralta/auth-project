package py.una.pol.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;

@Component
public class JwtUtil {
    public static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hora

    // Definir una clave secreta de 256 bits (32 bytes) de manera explícita y consistente.
    private static final String SECRET_KEY_STRING = "miClaveSecretaSuperSegura12345678"; // 32 caracteres (256 bits)
    private static final SecretKey SECRET_KEY = io.jsonwebtoken.security.Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes()); // Convertir a SecretKey

    // Método para generar un token con roles
    public String generateTokenWithRoles(String username, Set<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);  // Agregar roles al token
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256) // Usamos la misma clave secreta
                .compact();
    }

    // Extraer los roles del token JWT
    public Set<String> extractRoles(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    
        // Verifica si los roles están en formato lista y los convierte a Set
        Object rolesObject = claims.get("roles");
        if (rolesObject instanceof List<?>) {
            List<?> rolesList = (List<?>) rolesObject;
            return new HashSet<String>((List<String>) rolesList);  // Especifica el tipo String al crear HashSet
        }
    
        // Si los roles ya son un Set, devuelve el Set directamente
        return (Set<String>) rolesObject;
    }

    // Método para extraer el nombre de usuario del token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Validar si el token es válido
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // Verificar si el token ha expirado
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // Extraer todas las reclamaciones del token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY) // Usamos la misma clave secreta
                .parseClaimsJws(token)
                .getBody();
    }
}
