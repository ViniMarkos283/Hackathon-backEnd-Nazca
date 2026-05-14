package com.nazca.backend.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data @Builder
public class ColaboradorConformidadeResponse {
    private Integer colaboradorId;
    private String colaboradorNome;
    private String cargoNome;
    private String setorNome;
    private int totalPopsObrigatorios;
    private int concluidos;
    private int pendentes;
    private double percentualConformidade;
    private List<String> popsPendentes;
}