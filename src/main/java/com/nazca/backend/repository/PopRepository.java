package com.nazca.backend.repository;

import com.nazca.backend.model.Pop;
import com.nazca.backend.model.enums.PopStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PopRepository extends JpaRepository<Pop, Integer> {

    Optional<Pop> findByCodigo(String codigo);
    List<Pop> findByStatus(PopStatus status);
    long countByStatus(PopStatus status);

    @Query("SELECT p FROM Pop p WHERE p.dataValidade <= :limite AND p.status = 'ativo'")
    List<Pop> findVencendoAte(@Param("limite") LocalDate limite);

    @Query("SELECT p FROM Pop p WHERE p.dataValidade < CURRENT_DATE AND p.status = 'ativo'")
    List<Pop> findVencidos();

    @Query("SELECT p FROM Pop p WHERE p.dataValidade BETWEEN CURRENT_DATE AND :limite")
    List<Pop> findVencendoEmBreve(@Param("limite") LocalDate limite);

    @Query("SELECT COUNT(p) FROM Pop p")
    long countTotal();
}