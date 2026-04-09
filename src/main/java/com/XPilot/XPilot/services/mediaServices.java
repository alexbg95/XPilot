package com.XPilot.XPilot.services;

import java.util.List;
import com.XPilot.XPilot.models.media;

public interface mediaServices {

    List<media> all();

    media find(long cod);

    media save(media mda);

    media update(long cod, media mda);

    void delete(long cod);
}
