package com.nazca.backend.model;

import com.nazca.backend.model.enums.TreinamentoStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "treinamento")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
public class Treinamento {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pop_id", nullable = false)
    private Pop pop;

    @Column(name = "carga_horaria")
    private Integer cargaHoraria;

    @Column(name = "validade_meses")
    private Integer validadeMeses;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TreinamentoStatus status = TreinamentoStatus.concluido;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    void prePersist() { this.criadoEm = LocalDateTime.now(); }
}
