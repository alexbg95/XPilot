package com.XPilot.XPilot.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.XPilot.XPilot.models.Contratacion;

public interface ContratacionRepository extends JpaRepository<Contratacion, Long> {

    List<Contratacion> findByEstado(String estado);

    List<Contratacion> findByCliente_Id(Long id);

    List<Contratacion> findByArtista_Usuario_Id(Long id);

    // ✅ CORREGIDO: Integer → Long
    void deleteByArtista_Id(Long id);
}
