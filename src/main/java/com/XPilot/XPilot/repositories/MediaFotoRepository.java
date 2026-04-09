package com.XPilot.XPilot.repositories;

import com.XPilot.XPilot.models.MediaFoto;
import com.XPilot.XPilot.models.media;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MediaFotoRepository extends JpaRepository<MediaFoto, Long> {
    List<MediaFoto> findByMedia(media media);
    void deleteByMedia(media media);
}
