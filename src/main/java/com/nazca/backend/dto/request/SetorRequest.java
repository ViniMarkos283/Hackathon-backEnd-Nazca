package com.nazca.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SetorRequest {
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    private String descricao;
}