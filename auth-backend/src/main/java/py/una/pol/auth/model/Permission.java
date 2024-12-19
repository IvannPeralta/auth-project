package py.una.pol.auth.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

/**
 * Clase que representa un permiso en el sistema.
 * Es una entidad JPA que mapea la tabla "permissions" en la base de datos y define los atributos y relaciones asociados a un permiso.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
/* Renombrar la tabla */
@Table(name = "permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID único del permiso

    private String name; // Nombre del permiso

    private String description; // Descripción del permiso

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    /* Set de roles que poseen un permiso en especifico */
    private Set<Role> roles = new HashSet<>();
}