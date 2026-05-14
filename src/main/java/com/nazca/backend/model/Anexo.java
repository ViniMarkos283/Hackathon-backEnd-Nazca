package com.nazca.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "anexo")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
public class Anexo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pop_id", nullable = false)
    private Pop pop;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(name = "arquivo_url", nullable = false, columnDefinition = "TEXT")
    private String arquivoUrl;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    void prePersist() { this.criadoEm = LocalDateTime.now(); }
}
