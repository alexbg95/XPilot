package com.XPilot.XPilot.services;

import java.util.List;
import com.XPilot.XPilot.models.usuario;

public interface usuarioServices {
    List<usuario> all();
    usuario find(long cod);
    usuario save(usuario usr);
    usuario update(long cod, usuario usr);
    void delete(long cod);
}
