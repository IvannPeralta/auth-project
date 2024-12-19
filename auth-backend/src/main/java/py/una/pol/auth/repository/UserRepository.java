package py.una.pol.auth.repository;

import py.una.pol.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /* Metodo que busca un usuario por su nombre de usuario */
    User findByUsername(String username);
}