package com.zario.projetos.todo.repository;

import com.zario.projetos.todo.models.TarefaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TarefaRepository extends JpaRepository<TarefaModel, UUID> {
    List<TarefaModel> findByIsConcluida(Boolean isConcluida);
}
