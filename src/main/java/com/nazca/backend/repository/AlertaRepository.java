package com.nazca.backend.repository;

import com.nazca.backend.model.Alerta;
import com.nazca.backend.model.enums.AlertaTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Integer> {

    List<Alerta> findByLidoFalseOrderByCriadoEmDesc();
    List<Alerta> findByTipo(AlertaTipo tipo);
    List<Alerta> findAllByOrderByCriadoEmDesc();
    long countByLidoFalse();
}