package com.nazca.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evidencia_treinamento")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
public class EvidenciaTreinamento {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treinamento_colaborador_id", nullable = false)
    private TreinamentoColaborador treinamento;

    @Column(nullable = false, length = 30)
    private String tipo;

    @Column(name = "arquivo_url", nullable = false, columnDefinition = "TEXT")
    private String arquivoUrl;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    void prePersist() { this.criadoEm = LocalDateTime.now(); }
}