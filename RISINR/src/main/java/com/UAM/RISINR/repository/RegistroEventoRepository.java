package com.UAM.RISINR.repository;

import com.UAM.RISINR.model.RegistroEvento;
import com.UAM.RISINR.model.RegistroEventoPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistroEventoRepository extends JpaRepository <RegistroEvento, RegistroEventoPK>{
}
