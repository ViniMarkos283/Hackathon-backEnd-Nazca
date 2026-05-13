package com.nazca.repository;

import com.nazca.model.EvidenciaTreinamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EvidenciaRepository extends JpaRepository<EvidenciaTreinamento, Integer> {

    List<EvidenciaTreinamento> findByTreinamentoId(Integer treinamentoId);

    long countAll();
}