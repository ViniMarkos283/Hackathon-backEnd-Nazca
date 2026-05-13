package com.nazca.repository;

import com.nazca.model.Setor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SetorRepository extends JpaRepository<Setor, Integer> {

    List<Setor> findByAtivo(Boolean ativo);

    @Query("""
        SELECT s FROM Setor s
        WHERE s.ativo = true
        ORDER BY s.nome
    """)
    List<Setor> findAllAtivos();
}