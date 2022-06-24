package br.com.anthonini.estudoEstrategico.repository.helper.permissaousuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.anthonini.estudoEstrategico.model.Usuario;
import br.com.anthonini.estudoEstrategico.repository.helper.permissaousuario.filter.PermissaoUsuarioFilter;

public interface PermissaoUsuarioRepositoryQueries {

	public Page<Usuario> filtrar(PermissaoUsuarioFilter filter, Pageable pageable);
}
