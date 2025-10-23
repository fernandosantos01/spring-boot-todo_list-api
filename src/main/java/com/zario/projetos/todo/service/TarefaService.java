package com.zario.projetos.todo.service;

import com.zario.projetos.todo.dtos.TarefaAtualizacaoDTO;
import com.zario.projetos.todo.dtos.TarefaCriacaoDTO;
import com.zario.projetos.todo.exception.RecursoNaoEncontradoException;
import com.zario.projetos.todo.models.TarefaModel;
import com.zario.projetos.todo.repository.TarefaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TarefaService {
    @Autowired
    private TarefaRepository repository;

    public TarefaModel criarTarefa(TarefaCriacaoDTO dto) {
        var tarefaModel = new TarefaModel();
        BeanUtils.copyProperties(dto, tarefaModel);
        return repository.save(tarefaModel);
    }

    public List<TarefaModel> buscarTodas() {
        return repository.findAll();
    }

    public TarefaModel buscarPorId(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa com ID " + id + " não encontrada."));
    }

    public TarefaModel atualizarTarefa(TarefaModel tarefaExistente, TarefaAtualizacaoDTO dto) {
        tarefaExistente.setTitulo(dto.titulo());
        tarefaExistente.setDescricao(dto.descricao());
        if (dto.isConcluida() != null) {
            tarefaExistente.setIsConcluida(dto.isConcluida());
        }
        return repository.save(tarefaExistente);
    }

    public void deletarTarefa(TarefaModel tarefa) {
        repository.delete(tarefa);
    }

    public List<TarefaModel> tarefasPendentes() {
        return repository.findByIsConcluida(false);
    }
}
