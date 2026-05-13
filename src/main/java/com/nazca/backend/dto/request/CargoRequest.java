package com.nazca.dto.request;

import com.nazca.model.enums.NivelCargo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class CargoRequest {
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Setor é obrigatório")
    private Integer setorId;

    @NotNull(message = "Nível é obrigatório")
    private NivelCargo nivel;

    private String descricao;

    // IDs dos POPs obrigatórios para esse cargo
    private List<Integer> popIdsObrigatorios;
}