package py.una.pol.auth.repository;

import py.una.pol.auth.model.Permission;
import py.una.pol.auth.model.Role;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
// Consulta personalizada para obtener los permisos asignados a un rol
    @Query("SELECT p FROM Role r JOIN r.permissions p WHERE r.id = :roleId")
    List<Permission> findPermissionsByRoleId(@Param("roleId") Long roleId);
}
