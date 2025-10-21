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
import java.util.Optional;
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
    public ResponseEntity<Object> buscarPorId(@PathVariable(value = "id") UUID id) {
        Optional<TarefaModel> tarefaProcurada = service.buscarPorId(id);
        if (tarefaProcurada.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa não encontrada");
        }
        return ResponseEntity.status(HttpStatus.OK).body(tarefaProcurada.get());
    }

    @GetMapping
    public ResponseEntity<List<TarefaModel>> pegarTodasTarefas() {

//        while (!tarefaModelList.isEmpty()) {
//            for (TarefaModel tarefa : tarefaModelList) {
//                UUID id = tarefa.getId();
//                tarefaModelList.add(link)
//            }
//        }
        return ResponseEntity.status(HttpStatus.OK).body(service.buscarTodas());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> apagarTarefa(@PathVariable(value = "id") UUID id) {
        Optional<TarefaModel> tarefaDeletar = service.buscarPorId(id);
        if (tarefaDeletar.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa Não Encontrada!");
        }
        service.deletarTarefa(tarefaDeletar.get());
        return ResponseEntity.status(HttpStatus.OK).body("Tarefa Deletada");
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> atualizarTarefa(@PathVariable(value = "id") UUID id,
                                                  @RequestBody @Valid TarefaAtualizacaoDTO tarefaAtualizacaoDTO) {
        Optional<TarefaModel> tarefaProcurada = service.buscarPorId(id);
        if (tarefaProcurada.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa Não Encontrada!");
        }
        TarefaModel tarefaAtualizada = service.atualizarTarefa(tarefaProcurada.get(), tarefaAtualizacaoDTO);
        return ResponseEntity.status(HttpStatus.OK).body(tarefaAtualizada);
    }

    @GetMapping("/pendentes")
    public ResponseEntity<List<TarefaModel>> tarefasPendentes() {
        List<TarefaModel> tarefasPendentes = service.tarefasPendentes();
        if (tarefasPendentes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(tarefasPendentes);
        }
        return ResponseEntity.status(HttpStatus.OK).body(service.tarefasPendentes());
    }
}
