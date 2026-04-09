package com.XPilot.XPilot.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.XPilot.XPilot.models.usuario;
import com.XPilot.XPilot.repositories.usuarioRepository;

@Service
public class usuarioServicesManager implements usuarioServices {

    @Autowired
    private usuarioRepository repository;

    @Override
    public List<usuario> all() {
        return (List<usuario>) this.repository.findAll();
    }

    @Override
    public usuario find(long cod) {
        return this.repository.findById(cod).orElse(null);
    }

    @Override
    public usuario save(usuario usr) {
        return this.repository.save(usr);
    }

    @Override
    public usuario update(long cod, usuario usr) {
        usuario exist = this.find(cod);
        if (exist != null) {
            exist.setUname(usr.getUname());
            exist.setEmail(usr.getEmail());
            exist.setPassw(usr.getPassw());
            return this.save(exist);
        }
        return null;
    }

    @Override
    public void delete(long cod) {
        this.repository.deleteById(cod);
    }
}
