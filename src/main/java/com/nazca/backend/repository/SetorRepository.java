package com.nazca.backend.repository;

import com.nazca.backend.model.Setor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SetorRepository extends JpaRepository<Setor, Integer> {

    List<Setor> findByAtivo(Boolean ativo);
    Optional<Setor> findByNome(String nome);

    @Query("""
        SELECT s FROM Setor s
        WHERE s.ativo = true
        ORDER BY s.nome
    """)
    List<Setor> findAllAtivos();
}