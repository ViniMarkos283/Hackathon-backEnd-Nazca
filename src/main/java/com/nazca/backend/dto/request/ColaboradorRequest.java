package com.nazca.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ColaboradorRequest {
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private String email;
    private String matricula;

    @NotNull(message = "Cargo é obrigatório")
    private Integer cargoId;

    private LocalDate dataAdmissao;
}