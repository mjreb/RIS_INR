package com.UAM.RISINR.service.access.implementation;

import com.UAM.RISINR.repository.DatosAccesoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AccountLockedService {

    private final DatosAccesoRepository accesoRepo;

    public AccountLockedService(DatosAccesoRepository accesoRepo) {
        this.accesoRepo = accesoRepo;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void block(String usuarioId) {
        var usuario = accesoRepo.findByIdUsuarioID(usuarioId).get();
        usuario.setEstado("Bloqueado");
        accesoRepo.save(usuario);
    }
}
