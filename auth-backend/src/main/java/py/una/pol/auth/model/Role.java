package py.una.pol.auth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Clase queepresenta un rol en el sistema.
 * Es una entidad JPA que mapea la tabla "roles" en la base de datos
 * y define los atributos y relaciones asociadas a un rol.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
/* Renombrar la tabla */
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id unico que identifica el rol

    private String name; // Nombre del rol

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>(); // Conjunto de usuarios que tienen este rol

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_permissions", // Nombre de la tabla de unión para roles y permisos
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), // Columna en la tabla de unión para Role
            inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id") // Columna en la tabla de unión para Permission
    )

    
    /* Set de objetos Permisos que contiene un rol en especifico */
    private Set<Permission> permissions = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id != null && id.equals(role.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
