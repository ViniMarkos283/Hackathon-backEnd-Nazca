package com.nazca.dto.response;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class DashboardResponse {
    private long totalPops;
    private long totalColaboradoresAtivos;
    private long totalTreinamentosMesAtual;
    private long totalEvidencias;
    private long totalAlertas;
    private long alertasNaoLidos;
    private long popsVencendo30Dias;
    private long treinamentosAtrasados;
    private long totalCargos;
    private long totalSetores;
}