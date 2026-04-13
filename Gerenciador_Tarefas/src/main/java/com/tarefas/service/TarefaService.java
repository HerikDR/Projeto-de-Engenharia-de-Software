package com.tarefas.service;

import com.tarefas.dto.TarefaDTO.TarefaRequest;
import com.tarefas.dto.TarefaDTO.TarefaResponse;
import com.tarefas.model.Tarefa;
import com.tarefas.model.Usuario;
import com.tarefas.repository.TarefaRepository;
import com.tarefas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<TarefaResponse> listarPorUsuario(Long usuarioId) {
        validarUsuarioExistente(usuarioId);
        return tarefaRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TarefaResponse> listarPorUsuarioEStatus(Long usuarioId, boolean concluida) {
        validarUsuarioExistente(usuarioId);
        return tarefaRepository.findByUsuarioIdAndConcluida(usuarioId, concluida)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TarefaResponse buscarPorId(Long id) {
        return toResponse(buscarTarefaOuLancarExcecao(id));
    }

    @Transactional
    public TarefaResponse criar(TarefaRequest request) {
        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Usuário não encontrado com ID: " + request.usuarioId()));

        Tarefa tarefa = new Tarefa();
        tarefa.setUsuario(usuario);
        tarefa.setTitulo(request.titulo());
        tarefa.setDescricao(request.descricao());
        tarefa.setConcluida(request.concluida());

        return toResponse(tarefaRepository.save(tarefa));
    }

    @Transactional
    public TarefaResponse atualizar(Long id, TarefaRequest request) {
        Tarefa tarefa = buscarTarefaOuLancarExcecao(id);

        if (!tarefa.getUsuario().getId().equals(request.usuarioId())) {
            Usuario novoUsuario = usuarioRepository.findById(request.usuarioId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Usuário não encontrado com ID: " + request.usuarioId()));
            tarefa.setUsuario(novoUsuario);
        }

        tarefa.setTitulo(request.titulo());
        tarefa.setDescricao(request.descricao());
        tarefa.setConcluida(request.concluida());

        return toResponse(tarefaRepository.save(tarefa));
    }

    @Transactional
    public TarefaResponse alternarConcluida(Long id) {
        Tarefa tarefa = buscarTarefaOuLancarExcecao(id);
        tarefa.setConcluida(!tarefa.isConcluida());
        return toResponse(tarefaRepository.save(tarefa));
    }

    @Transactional
    public void deletar(Long id) {
        Tarefa tarefa = buscarTarefaOuLancarExcecao(id);
        tarefaRepository.delete(tarefa);
    }

    // --- Helpers ---

    private Tarefa buscarTarefaOuLancarExcecao(Long id) {
        return tarefaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada com ID: " + id));
    }

    private void validarUsuarioExistente(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new IllegalArgumentException("Usuário não encontrado com ID: " + usuarioId);
        }
    }

    private TarefaResponse toResponse(Tarefa tarefa) {
        return new TarefaResponse(
                tarefa.getId(),
                tarefa.getUsuario().getId(),
                tarefa.getUsuario().getNome(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.isConcluida(),
                tarefa.getCriadoEm() != null ? tarefa.getCriadoEm().toString() : null,
                tarefa.getAtualizadoEm() != null ? tarefa.getAtualizadoEm().toString() : null
        );
    }
}
