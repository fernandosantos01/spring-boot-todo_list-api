package com.zario.projetos.todo.models;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tarefas")
@Data
public class TarefaModel {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    private String titulo;
    private String descricao;
    private Boolean isConcluida = false;

    private LocalDateTime dataCriacao = LocalDateTime.now();
}
