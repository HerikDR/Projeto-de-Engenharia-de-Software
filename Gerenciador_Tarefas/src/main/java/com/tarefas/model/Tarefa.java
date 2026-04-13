package com.tarefas.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "tarefas")
@Getter
@Setter
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    private boolean concluida = false;

    @Column(name = "criado_em")
    private OffsetDateTime criadoEm;

    @Column(name = "atualizado_em")
    private OffsetDateTime atualizadoEm;

    @PrePersist
    public void prePersist() {
        criadoEm = OffsetDateTime.now();
        atualizadoEm = OffsetDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        atualizadoEm = OffsetDateTime.now();
    }
}
