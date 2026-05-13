package com.nazca.repository;

import com.nazca.model.PopCargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PopCargoRepository extends JpaRepository<PopCargo, Integer> {

    List<PopCargo> findByCargoId(Integer cargoId);
    List<PopCargo> findByPopId(Integer popId);
    List<PopCargo> findByCargoIdAndObrigatorioTrue(Integer cargoId);
    boolean existsByPopIdAndCargoId(Integer popId, Integer cargoId);
}