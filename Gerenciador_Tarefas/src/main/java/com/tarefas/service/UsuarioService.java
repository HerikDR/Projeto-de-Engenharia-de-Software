package com.tarefas.service;

import com.tarefas.dto.UsuarioDTO.UsuarioRequest;
import com.tarefas.dto.UsuarioDTO.UsuarioResponse;
import com.tarefas.model.Usuario;
import com.tarefas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id) {
        return toResponse(buscarUsuarioOuLancarExcecao(id));
    }

    @Transactional
    public UsuarioResponse criar(UsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Já existe um usuário com o e-mail: " + request.email());
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
       
        usuario.setSenha(request.senha());

        return toResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioResponse atualizar(Long id, UsuarioRequest request) {
        Usuario usuario = buscarUsuarioOuLancarExcecao(id);

        boolean emailAlterado = !usuario.getEmail().equalsIgnoreCase(request.email());
        if (emailAlterado && usuarioRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Já existe um usuário com o e-mail: " + request.email());
        }

        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenha(request.senha());

        return toResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public void deletar(Long id) {
        Usuario usuario = buscarUsuarioOuLancarExcecao(id);
        usuarioRepository.delete(usuario);
    }

    // --- Helpers ---

    private Usuario buscarUsuarioOuLancarExcecao(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com ID: " + id));
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail()
        );
    }
}
