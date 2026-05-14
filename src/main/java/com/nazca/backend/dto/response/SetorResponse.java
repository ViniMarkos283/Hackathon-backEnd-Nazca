package com.nazca.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SetorResponse {
    private Integer id;
    private String nome;
    private String descricao;
    private Boolean ativo;
    private Long totalPops;
    private Long totalColaboradores;
}
