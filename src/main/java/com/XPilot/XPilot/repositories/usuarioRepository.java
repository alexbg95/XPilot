package com.XPilot.XPilot.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.XPilot.XPilot.models.usuario;

@Repository
public interface usuarioRepository extends JpaRepository<usuario, Long> {

    Optional<usuario> findByEmail(String email);

    Optional<usuario> findByUname(String uname);

    boolean existsByEmail(String email);
    boolean existsByUname(String uname);

    usuario findByRol(String rol);
}
