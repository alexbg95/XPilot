package com.XPilot.XPilot.repositories;

import com.XPilot.XPilot.models.Notificacion;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findByUsuarioIdAndLeidoFalse(Long userId);

    long countByUsuarioIdAndLeidoFalse(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE Notificacion n SET n.leido = true WHERE n.id = :id")
    void marcarLeida(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Notificacion n SET n.leido = true WHERE n.usuario.id = :userId")
    void marcarTodas(@Param("userId") Long userId);

}