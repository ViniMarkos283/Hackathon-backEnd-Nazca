package com.nazca.repository;

import com.nazca.model.Cargo;
import com.nazca.model.Setor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Integer> {

    List<Cargo> findByAtivoTrue();
    List<Cargo> findBySetor(Setor setor);
    List<Cargo> findBySetorId(Integer setorId);
}