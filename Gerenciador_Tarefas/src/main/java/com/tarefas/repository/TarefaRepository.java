package com.tarefas.repository;

import com.tarefas.model.Tarefa;
import com.tarefas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    List<Tarefa> findByUsuario(Usuario usuario);

    List<Tarefa> findByUsuarioAndConcluida(Usuario usuario, boolean concluida);

    List<Tarefa> findByUsuarioOrderByCriadoEmDesc(Usuario usuario);
}