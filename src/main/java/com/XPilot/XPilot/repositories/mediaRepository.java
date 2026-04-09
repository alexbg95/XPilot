package com.XPilot.XPilot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.XPilot.XPilot.models.media;

// ✅ CORREGIDO: Integer → Long para coincidir con media.id
public interface mediaRepository extends JpaRepository<media, Long> {
}
