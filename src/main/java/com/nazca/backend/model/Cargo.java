package com.nazca.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nazca.model.enums.NivelCargo;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "cargo")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
public class Cargo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setor_id", nullable = false)
    private Setor setor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelCargo nivel = NivelCargo.operacional;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    void prePersist() { this.criadoEm = LocalDateTime.now(); }

    @JsonIgnore
    @OneToMany(mappedBy = "cargo", fetch = FetchType.LAZY)
    private List<Colaborador> colaboradores;

    @JsonIgnore
    @OneToMany(mappedBy = "cargo", fetch = FetchType.LAZY)
    private List<PopCargo> popCargos;
}