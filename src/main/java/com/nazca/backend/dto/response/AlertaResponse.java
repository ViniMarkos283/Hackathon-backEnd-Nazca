package com.nazca.dto.response;

import com.nazca.model.enums.AlertaTipo;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class AlertaResponse {
    private Integer id;
    private AlertaTipo tipo;
    private String mensagem;
    private Boolean lido;
    private Integer referenciaId;
    private String referenciaTipo;
    private LocalDateTime criadoEm;
}