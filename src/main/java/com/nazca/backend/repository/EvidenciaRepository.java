package com.nazca.backend.repository;

import com.nazca.backend.model.EvidenciaTreinamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EvidenciaRepository extends JpaRepository<EvidenciaTreinamento, Integer> {

    List<EvidenciaTreinamento> findByTreinamentoId(Integer treinamentoId);

    long count();
}