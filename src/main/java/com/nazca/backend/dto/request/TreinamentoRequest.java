package com.nazca.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class TreinamentoRequest {
    @NotNull
    private Integer colaboradorId;

    @NotNull
    private Integer popId;

    @NotNull
    private LocalDate dataConclusao;

    @NotNull
    private LocalDate validadeTrein;

    private String evidenciaUrl;
}