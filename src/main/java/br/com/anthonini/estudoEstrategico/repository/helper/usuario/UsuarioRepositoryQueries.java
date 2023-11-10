package br.com.anthonini.estudoEstrategico.repository.helper.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.anthonini.estudoEstrategico.model.Usuario;
import br.com.anthonini.estudoEstrategico.repository.helper.usuario.filter.UsuarioFilter;

public interface UsuarioRepositoryQueries {

	public Optional<Usuario> buscarAtivoPorEmail(String email);
	
	public List<String> permissoes(Usuario usuario);
	
	public Page<Usuario> filtrar(UsuarioFilter filter, Pageable pageable);
}
