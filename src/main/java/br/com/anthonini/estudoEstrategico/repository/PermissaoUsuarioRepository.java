package br.com.anthonini.estudoEstrategico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.anthonini.estudoEstrategico.model.GrupoUsuario;
import br.com.anthonini.estudoEstrategico.repository.helper.permissaousuario.PermissaoUsuarioRepositoryQueries;

@Repository
public interface PermissaoUsuarioRepository extends JpaRepository<GrupoUsuario, Long>, PermissaoUsuarioRepositoryQueries {

}
