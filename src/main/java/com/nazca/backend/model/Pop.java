package com.nazca.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nazca.backend.model.enums.PopStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pop")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
public class Pop {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 20, unique = true)
    private String codigo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setor_id", nullable = false)
    private Setor setor;

    @Column(nullable = false, length = 10)
    private String versao = "1";

    @Column(name = "data_validade")
    private LocalDate dataValidade;

    @Enumerated(EnumType.STRING)
    private PopStatus status = PopStatus.ativo;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @PrePersist
    void prePersist() { if (this.dataCriacao == null) this.dataCriacao = LocalDateTime.now(); }

    @JsonIgnore
    @OneToMany(mappedBy = "pop", fetch = FetchType.LAZY)
    private List<PopCargo> popCargos;

    @JsonIgnore
    @OneToMany(mappedBy = "pop", fetch = FetchType.LAZY)
    private List<TreinamentoColaborador> treinamentos;
}