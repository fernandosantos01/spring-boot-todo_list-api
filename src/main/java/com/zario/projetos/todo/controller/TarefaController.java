package com.zario.projetos.todo.controller;

import com.zario.projetos.todo.dtos.TarefaCriacaoDTO;
import com.zario.projetos.todo.dtos.TarefaAtualizacaoDTO;
import com.zario.projetos.todo.models.TarefaModel;
import com.zario.projetos.todo.service.TarefaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {
    @Autowired
    private TarefaService service;

    @PostMapping
    public ResponseEntity<TarefaModel> criarTarefa(@RequestBody @Valid TarefaCriacaoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarTarefa(dto));
    }

    @GetMapping("{id}")
    public ResponseEntity<TarefaModel> buscarPorId(@PathVariable(value = "id") UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<TarefaModel>> pegarTodasTarefas() {
        return ResponseEntity.status(HttpStatus.OK).body(service.buscarTodas());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> apagarTarefa(@PathVariable(value = "id") UUID id) {
        TarefaModel tarefaDeletar = service.buscarPorId(id);
        service.deletarTarefa(tarefaDeletar);
        return ResponseEntity.status(HttpStatus.OK).body("Tarefa Deletada");
    }

    @PutMapping("{id}")
    public ResponseEntity<TarefaModel> atualizarTarefa(@PathVariable(value = "id") UUID id,
                                                  @RequestBody @Valid TarefaAtualizacaoDTO tarefaAtualizacaoDTO) {
        TarefaModel tarefaExistente = service.buscarPorId(id);
        TarefaModel tarefaAtualizada = service.atualizarTarefa(tarefaExistente, tarefaAtualizacaoDTO);
        return ResponseEntity.status(HttpStatus.OK).body(tarefaAtualizada);
    }

    @GetMapping("/pendentes")
    public ResponseEntity<List<TarefaModel>> tarefasPendentes() {
        return ResponseEntity.status(HttpStatus.OK).body(service.tarefasPendentes());
    }
}
