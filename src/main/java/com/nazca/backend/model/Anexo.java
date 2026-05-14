package com.nazca.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "anexo")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
public class Anexo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pop_codigo", referencedColumnName = "codigo", nullable = false)
    private Pop pop;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "data_criacao")
    private LocalDate dataCriacao;

    @Column(name = "data_validade")
    private LocalDate dataValidade;

    @Column(length = 20)
    private String status = "ativo";

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    void prePersist() { this.criadoEm = LocalDateTime.now(); }
}