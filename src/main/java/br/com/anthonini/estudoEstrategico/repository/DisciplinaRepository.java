package br.com.anthonini.estudoEstrategico.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.anthonini.estudoEstrategico.model.Disciplina;
import br.com.anthonini.estudoEstrategico.model.Usuario;
import br.com.anthonini.estudoEstrategico.repository.helper.disciplina.DisciplinaRepositoryQueries;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long>, DisciplinaRepositoryQueries {

	Optional<Disciplina> findByNomeIgnoreCaseAndUsuario(String nome, Usuario usuario);
}
