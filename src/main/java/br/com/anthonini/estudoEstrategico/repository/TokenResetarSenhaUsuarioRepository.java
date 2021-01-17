package br.com.anthonini.estudoEstrategico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.anthonini.estudoEstrategico.model.TokenResetarSenhaUsuario;
import br.com.anthonini.estudoEstrategico.model.Usuario;

@Repository
public interface TokenResetarSenhaUsuarioRepository extends JpaRepository<TokenResetarSenhaUsuario, Long> {

	TokenResetarSenhaUsuario findByToken(String token);
	
	List<TokenResetarSenhaUsuario> findByAtivoTrueAndUsuario(Usuario usuario);

}
