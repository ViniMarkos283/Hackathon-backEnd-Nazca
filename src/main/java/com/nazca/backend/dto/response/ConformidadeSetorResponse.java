package com.nazca.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class ConformidadeSetorResponse {
    private String setorNome;
    private long totalTreinamentos;
    private long concluidos;
    private double percentual;
}