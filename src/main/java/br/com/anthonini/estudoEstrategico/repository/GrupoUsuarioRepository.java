package br.com.anthonini.estudoEstrategico.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.anthonini.estudoEstrategico.model.GrupoUsuario;
import br.com.anthonini.estudoEstrategico.repository.helper.grupousuario.GrupoUsuarioRepositoryQueries;

@Repository
public interface GrupoUsuarioRepository extends JpaRepository<GrupoUsuario, Long>, GrupoUsuarioRepositoryQueries {

	Optional<GrupoUsuario> findByNomeIgnoreCase(String nome);

}
