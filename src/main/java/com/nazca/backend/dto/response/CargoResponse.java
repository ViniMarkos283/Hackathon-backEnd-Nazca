package com.nazca.backend.dto.response;

import com.nazca.backend.model.enums.NivelCargo;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data @Builder
public class CargoResponse {
    private Integer id;
    private String nome;
    private String setorNome;
    private Integer setorId;
    private NivelCargo nivel;
    private String descricao;
    private Boolean ativo;
    private long totalColaboradores;
    private List<PopResumo> popsObrigatorios;

    @Data @Builder
    public static class PopResumo {
        private Integer id;
        private String codigo;
        private String descricao;
        private String status;
    }
}