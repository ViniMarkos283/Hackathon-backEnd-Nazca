package com.nazca.dto.request;

import com.nazca.model.enums.PopStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PopRequest {
    @NotBlank(message = "Código é obrigatório")
    private String codigo;

    private String descricao;

    @NotNull(message = "Setor é obrigatório")
    private Integer setorId;

    private String versao = "1";
    private LocalDate dataValidade;
    private PopStatus status = PopStatus.ativo;
}