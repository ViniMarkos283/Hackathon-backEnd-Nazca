package com.nazca.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data @Builder
public class ColaboradorResponse {
    private Integer id;
    private String nome;
    private String email;
    private String matricula;
    private String cargoNome;
    private String cargoNivel;
    private String setorNome;
    private LocalDate dataAdmissao;
    private Boolean ativo;
    private List<String> popsObrigatorios;
}