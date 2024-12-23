package py.una.pol.auth.repository;

import py.una.pol.auth.model.User;
import py.una.pol.auth.model.Role;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /* Metodo que busca un usuario por su nombre de usuario */
    User findByUsername(String username);
    /* Consulta para encontrar roles por id de usuari */
    @Query("SELECT r FROM User u JOIN u.roles r WHERE u.id = :userId")
    List<Role> findRolesByUserId(@Param("userId") Long userId);
}