package com.tarefas.controller;

import com.tarefas.dto.TarefaDTO.TarefaRequest;
import com.tarefas.dto.TarefaDTO.TarefaResponse;
import com.tarefas.service.TarefaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarefas")
@RequiredArgsConstructor
public class TarefaController {

    private final TarefaService tarefaService;

    /**
     * GET /api/tarefas?usuarioId=1
     * GET /api/tarefas?usuarioId=1&concluida=true
     */
    @GetMapping
    public ResponseEntity<List<TarefaResponse>> listar(
            @RequestParam Long usuarioId,
            @RequestParam(required = false) Boolean concluida) {

        List<TarefaResponse> tarefas = (concluida != null)
                ? tarefaService.listarPorUsuarioEStatus(usuarioId, concluida)
                : tarefaService.listarPorUsuario(usuarioId);

        return ResponseEntity.ok(tarefas);
    }

    /**
     * GET /api/tarefas/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TarefaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tarefaService.buscarPorId(id));
    }

    /**
     * POST /api/tarefas
     */
    @PostMapping
    public ResponseEntity<TarefaResponse> criar(@Valid @RequestBody TarefaRequest request) {
        TarefaResponse criada = tarefaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    /**
     * PUT /api/tarefas/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<TarefaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TarefaRequest request) {
        return ResponseEntity.ok(tarefaService.atualizar(id, request));
    }

    /**
     * PATCH /api/tarefas/{id}/toggle
     * Alterna o status concluída/pendente da tarefa
     */
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TarefaResponse> toggleConcluida(@PathVariable Long id) {
        return ResponseEntity.ok(tarefaService.alternarConcluida(id));
    }

    /**
     * DELETE /api/tarefas/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        tarefaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
