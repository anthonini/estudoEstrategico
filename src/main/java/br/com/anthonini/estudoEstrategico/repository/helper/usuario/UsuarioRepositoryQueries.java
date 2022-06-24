package br.com.anthonini.estudoEstrategico.repository.helper.usuario;

import java.util.List;
import java.util.Optional;

import br.com.anthonini.estudoEstrategico.model.Usuario;

public interface UsuarioRepositoryQueries {

	public Optional<Usuario> buscarAtivoPorEmail(String email);
	
	public List<String> permissoes(Usuario usuario);
}
