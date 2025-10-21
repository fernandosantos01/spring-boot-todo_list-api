package com.zario.projetos.todo.service;

import com.zario.projetos.todo.dtos.TarefaAtualizacaoDTO;
import com.zario.projetos.todo.dtos.TarefaCriacaoDTO;
import com.zario.projetos.todo.models.TarefaModel;
import com.zario.projetos.todo.repository.TarefaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(TarefaService.class)
@ActiveProfiles("test")
public class TarefaServiceIntegrationTest {

    @Autowired
    private TarefaService service;

    @Autowired
    private TarefaRepository repository;

    private TarefaCriacaoDTO dtoCriacao;

    @BeforeEach
    void setup() {
        repository.deleteAll();
        dtoCriacao = new TarefaCriacaoDTO("Comprar Leite", "Ir ao mercado antes das 18h");
    }

    @Test
    void criarTarefa_deveSalvarComStatusPendente() {
        TarefaModel tarefaSalva = service.criarTarefa(dtoCriacao);

        //Verififa se a tarefa foi salva com sucesso e se o ID foi gerado
        assertThat(tarefaSalva).isNotNull();
        assertThat(tarefaSalva.getId()).isNotNull();
        // Verifica a regra de negócio: a tarefa deve ser salva como PENDENTE (false)
        assertFalse(tarefaSalva.getIsConcluida());
    }

    @Test
    void buscarPorId_deveRetornarTarefaExistente() {
        TarefaModel novaTarefa = new TarefaModel();
        novaTarefa.setTitulo("Revisar Código");
        novaTarefa.setDescricao("Revisão");
        novaTarefa.setIsConcluida(false);
        TarefaModel tarefaExistente = repository.save(novaTarefa);

        Optional<TarefaModel> resultado = service.buscarPorId(tarefaExistente.getId());

        assertTrue(resultado.isPresent());
        assertThat(resultado.get().getTitulo()).isEqualTo("Revisar Código");
    }

    @Test
    void buscarPorId_deveRetornarVazioParaIdInexistente() {
        UUID idInexistente = UUID.randomUUID();

        Optional<TarefaModel> resultado = service.buscarPorId(idInexistente);

        assertFalse(resultado.isPresent());
    }

    @Test
    void buscarTodas_deveRetornarTodasAsTarefas() {
        // ARRANGE
        TarefaModel tarefaModel1 = new TarefaModel();
        TarefaModel tarefaModel2 = new TarefaModel();

        tarefaModel1.setTitulo("T1");
        tarefaModel1.setDescricao("D1");
        tarefaModel1.setIsConcluida(false);

        tarefaModel2.setTitulo("T2");
        tarefaModel2.setDescricao("D2");
        tarefaModel2.setIsConcluida(false);

        repository.save(tarefaModel1);
        repository.save(tarefaModel2);

        // ACT
        List<TarefaModel> todasTarefas = service.buscarTodas();

        // ASSERT
        assertThat(todasTarefas).hasSize(2);
    }

    @Test
    void atualizarTarefa_deveAlterarTituloEDescricao() {
        // ARRANGE
        TarefaModel tarefaAntiga = service.criarTarefa(dtoCriacao);
        TarefaAtualizacaoDTO dtoAtualizacao = new TarefaAtualizacaoDTO("Novo Titulo", "Nova Descricao", null);

        // ACT
        TarefaModel tarefaAtualizada = service.atualizarTarefa(tarefaAntiga, dtoAtualizacao);

        // ASSERT
        assertThat(tarefaAtualizada.getTitulo()).isEqualTo("Novo Titulo");
        assertThat(tarefaAtualizada.getDescricao()).isEqualTo("Nova Descricao");
        // Garante que o status 'false' original foi PRESERVADO (pois o DTO enviou null)
        assertFalse(tarefaAtualizada.getIsConcluida());
    }

    @Test
    void atualizarTarefa_deveMarcarComoConcluida() {
        // ARRANGE
        TarefaModel tarefaAntiga = service.criarTarefa(dtoCriacao);
        // Envia o DTO explicitamente marcando como concluída
        TarefaAtualizacaoDTO dtoConcluida = new TarefaAtualizacaoDTO("Comprar Leite", "Ir ao mercado", true);

        // ACT
        TarefaModel tarefaAtualizada = service.atualizarTarefa(tarefaAntiga, dtoConcluida);

        // ASSERT
        assertTrue(tarefaAtualizada.getIsConcluida());
    }

    // =========================================================================
    // TESTES DE DELEÇÃO (DELETE)
    // =========================================================================

    @Test
    void deletarTarefa_deveRemoverTarefaDoBanco() {
        // ARRANGE
        TarefaModel tarefaParaDeletar = service.criarTarefa(dtoCriacao);
        UUID id = tarefaParaDeletar.getId();

        // ACT
        service.deletarTarefa(tarefaParaDeletar);

        // ASSERT
        // Tenta buscar e verifica se o resultado está vazio
        Optional<TarefaModel> resultado = repository.findById(id);
        assertFalse(resultado.isPresent());
    }

    @Test
    void tarefasPendentes_deveRetornarApenasTarefasNaoConcluidas() {

        // ARRANGE (Preparação): Criar cenários para provar o filtro

        // 1. Tarefa Pendente (DEVE SER CONTADA)
        TarefaModel pendente1 = new TarefaModel();
        pendente1.setTitulo("Pendente 1");
        pendente1.setIsConcluida(false); // Definindo explicitamente como false
        repository.save(pendente1);

        // 2. Tarefa Concluída (DEVE SER IGNORADA PELO FILTRO)
        TarefaModel concluida1 = new TarefaModel();
        concluida1.setTitulo("Concluída 1");
        concluida1.setIsConcluida(true); // Definindo explicitamente como true
        repository.save(concluida1);

        // 3. Tarefa Pendente (DEVE SER CONTADA)
        TarefaModel pendente2 = new TarefaModel();
        pendente2.setTitulo("Pendente 2");
        pendente2.setIsConcluida(false);
        repository.save(pendente2);

        // ACT (Ação): Chamar o método de filtragem
        List<TarefaModel> resultado = service.tarefasPendentes();

        // ASSERT (Verificação):
        // 1. O tamanho da lista DEVE ser 2 (apenas P1 e P2)
        assertThat(resultado).hasSize(2);

        // 2. Prova que o filtro está funcionando: verifica se a tarefa concluída não está na lista
        assertThat(resultado).extracting(TarefaModel::getTitulo)
                .containsExactlyInAnyOrder("Pendente 1", "Pendente 2");
    }

    @Test
    void marcarUmaTarefaComoConcluida() {
        TarefaModel tarefaOriginal = service.criarTarefa(dtoCriacao);
        TarefaAtualizacaoDTO dtoAtualizacao = new TarefaAtualizacaoDTO(tarefaOriginal.getTitulo(), tarefaOriginal.getDescricao(), true);
        TarefaModel tarefaAtualizada = service.atualizarTarefa(tarefaOriginal, dtoAtualizacao);

        assertTrue(tarefaAtualizada.getIsConcluida());
    }
}
