package com.nazca.backend.dto.response;

import com.nazca.backend.model.enums.TreinamentoStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data @Builder
public class TreinamentoResponse {
    private Integer id;
    private String colaboradorNome;
    private Integer colaboradorId;
    private String popCodigo;
    private String popDescricao;
    private LocalDate dataConclusao;
    private LocalDate validadeTrein;
    private TreinamentoStatus status;
    private String evidenciaUrl;
}