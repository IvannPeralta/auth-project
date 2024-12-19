package py.una.pol.auth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;


 /* * Clase que representa un usuario en el sistema.
 * Es una entidad JPA que mapea la tabla "users" en la base de datos
 * y define los atributos y relaciones asociadas a un usuario. */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
/* renombrar la tabla */
@Table(name = "users") 
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) /* id de usuario autogenerado e incremental */
    private Long id;  /* id del usuario */
    private String username;/* nombre de usuario */
    private String password; /* contraseña del usuario */

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles", //tabla de unión de usuarios y sus roles asociados
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), // Columna en la tabla de unión para User
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id") // Columna en la tabla de unión para Rol
    )
    /* Set de roles que posee un usuario en especifico */
    private Set<Role> roles = new HashSet<>(); 

    // Sobrescribir equals y hashCode para evitar duplicados en el Set
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id != null && id.equals(user.id); // Compara los usuarios por su ID único
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0; // Genera el hashCode basándose en el ID
    }
}
