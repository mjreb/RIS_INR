package com.UAM.RISINR.service.shared.implementations;

import com.UAM.RISINR.model.RegistroEvento;
import com.UAM.RISINR.model.RegistroEventoPK;
import com.UAM.RISINR.repository.RegistroEventoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistroEventoLogger {

    private final RegistroEventoRepository registroEventoRepo;

    // Inyección por constructor (sin Lombok, sin @Autowired — Spring detecta único constructor)
    public RegistroEventoLogger(RegistroEventoRepository registroEventoRepo) {
        this.registroEventoRepo = registroEventoRepo;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(int eventoId, int aplicacionId, long horaMs, String datosJson) {
        // Reintento simple por posible colisión de PK en el mismo milisegundo
        for (int i = 0; i < 5; i++) {
            try {
                RegistroEventoPK pk = new RegistroEventoPK(eventoId, aplicacionId, horaMs + i);
                RegistroEvento re = new RegistroEvento(pk, datosJson);
                registroEventoRepo.save(re);
                return;
            } catch (DataIntegrityViolationException e) {
            }
        }
    }
}
