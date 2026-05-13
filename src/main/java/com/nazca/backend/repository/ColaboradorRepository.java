package com.nazca.repository;

import com.nazca.model.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ColaboradorRepository extends JpaRepository<Colaborador, Integer> {

    List<Colaborador> findByAtivoTrue();

    @Query("SELECT c FROM Colaborador c JOIN FETCH c.cargo ca JOIN FETCH ca.setor WHERE c.ativo = true")
    List<Colaborador> findAllAtivosComCargoESetor();

    @Query("SELECT c FROM Colaborador c WHERE c.cargo.setor.id = :setorId AND c.ativo = true")
    List<Colaborador> findBySetorId(@Param("setorId") Integer setorId);

    long countByAtivoTrue();

    @Query("""
        SELECT DISTINCT c FROM Colaborador c
        WHERE c.ativo = true
        AND NOT EXISTS (
            SELECT pc FROM PopCargo pc
            WHERE pc.cargo = c.cargo
            AND pc.obrigatorio = true
            AND NOT EXISTS (
                SELECT tc FROM TreinamentoColaborador tc
                WHERE tc.colaborador = c
                AND tc.pop = pc.pop
                AND tc.status = 'concluido'
                AND tc.validadeTrein >= CURRENT_DATE
            )
        )
    """)
    List<Colaborador> findColaboradoresComTodosPops();
}