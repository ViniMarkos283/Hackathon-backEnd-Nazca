package com.nazca.model;

import com.nazca.model.enums.TreinamentoStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "treinamento_colaborador")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
public class TreinamentoColaborador {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colaborador_id", nullable = false)
    private Colaborador colaborador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pop_id", nullable = false)
    private Pop pop;

    @Column(name = "data_conclusao", nullable = false)
    private LocalDate dataConclusao;

    @Column(name = "validade_trein", nullable = false)
    private LocalDate validadeTrein;

    @Column(name = "evidencia_url", length = 255)
    private String evidenciaUrl;

    @Enumerated(EnumType.STRING)
    private TreinamentoStatus status = TreinamentoStatus.concluido;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    void prePersist() { this.criadoEm = LocalDateTime.now(); }
}