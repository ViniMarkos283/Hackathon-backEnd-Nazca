package com.nazca.backend.repository;

import com.nazca.backend.model.TreinamentoColaborador;
import com.nazca.backend.model.enums.TreinamentoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TreinamentoRepository extends JpaRepository<TreinamentoColaborador, Integer> {

    List<TreinamentoColaborador> findByColaboradorId(Integer colaboradorId);
    List<TreinamentoColaborador> findByStatus(TreinamentoStatus status);

    @Query("""
        SELECT tc FROM TreinamentoColaborador tc
        WHERE tc.colaborador.id = :colaboradorId
        AND tc.status = 'concluido'
        AND tc.validadeTrein >= CURRENT_DATE
    """)
    List<TreinamentoColaborador> findConcluidos(@Param("colaboradorId") Integer colaboradorId);

    @Query("""
        SELECT tc FROM TreinamentoColaborador tc
        WHERE MONTH(tc.dataConclusao) = MONTH(CURRENT_DATE)
        AND YEAR(tc.dataConclusao) = YEAR(CURRENT_DATE)
        AND tc.status = 'concluido'
    """)
    List<TreinamentoColaborador> findConcluidosNoMesAtual();

    @Query("""
        SELECT tc FROM TreinamentoColaborador tc
        ORDER BY tc.dataConclusao DESC
    """)
    List<TreinamentoColaborador> findAllOrderByDataDesc();

    @Query("""
        SELECT tc FROM TreinamentoColaborador tc
        WHERE tc.validadeTrein < CURRENT_DATE
        AND tc.status <> 'vencido'
    """)
    List<TreinamentoColaborador> findAtrasados();

    // Treinamentos pendentes: POPs obrigatórios sem treinamento concluído e válido
    @Query("""
        SELECT pc FROM PopCargo pc
        WHERE pc.obrigatorio = true
        AND NOT EXISTS (
            SELECT tc FROM TreinamentoColaborador tc
            WHERE tc.colaborador.id = :colaboradorId
            AND tc.pop = pc.pop
            AND tc.status = 'concluido'
            AND tc.validadeTrein >= CURRENT_DATE
        )
        AND pc.cargo.id = (
            SELECT c.cargo.id FROM Colaborador c WHERE c.id = :colaboradorId
        )
    """)
    List<com.nazca.backend.model.PopCargo> findTreinamentosPendentes(@Param("colaboradorId") Integer colaboradorId);
}