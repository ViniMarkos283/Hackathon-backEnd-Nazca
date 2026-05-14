package com.nazca.backend.dto.response;

import com.nazca.backend.model.enums.PopStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder
public class PopResponse {
    private Integer id;
    private String codigo;
    private String descricao;
    private String setorNome;
    private String versao;
    private LocalDate dataValidade;
    private PopStatus status;
    private String statusLabel;   // Vigente / Vencido / Em Revisão / Vence em Breve
    private Long diasParaVencer;
    private LocalDateTime dataCriacao;
}