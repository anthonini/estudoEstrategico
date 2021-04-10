package br.com.anthonini.estudoEstrategico.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.anthonini.estudoEstrategico.model.CicloEstudos;
import br.com.anthonini.estudoEstrategico.model.Usuario;
import br.com.anthonini.estudoEstrategico.repository.helper.cicloEstudos.CicloEstudosRepositoryQueries;

@Repository
public interface CicloEstudosRepository extends JpaRepository<CicloEstudos, Long>, CicloEstudosRepositoryQueries {

	Optional<CicloEstudos> findByNomeIgnoreCaseAndUsuario(String nome, Usuario usuario);
}
