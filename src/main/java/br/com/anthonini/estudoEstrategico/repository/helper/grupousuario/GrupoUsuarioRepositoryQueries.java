package br.com.anthonini.estudoEstrategico.repository.helper.grupousuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.anthonini.estudoEstrategico.model.GrupoUsuario;
import br.com.anthonini.estudoEstrategico.repository.helper.grupousuario.filter.GrupoUsuarioFilter;

public interface GrupoUsuarioRepositoryQueries {

	public Page<GrupoUsuario> filtrar(GrupoUsuarioFilter filter, Pageable pageable);
}
