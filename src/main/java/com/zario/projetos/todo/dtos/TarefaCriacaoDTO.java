package com.zario.projetos.todo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TarefaCriacaoDTO(@NotBlank@NotNull String titulo, @NotNull String descricao) {
}
