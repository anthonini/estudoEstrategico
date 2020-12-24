package br.com.anthonini.estudoEstrategico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.anthonini.estudoEstrategico.model.TokenConfirmacaoUsuario;
import br.com.anthonini.estudoEstrategico.model.Usuario;

@Repository
public interface TokenConfirmacaoUsuarioRepository extends JpaRepository<TokenConfirmacaoUsuario, Long> {

	public TokenConfirmacaoUsuario findByToken(String token);
	public TokenConfirmacaoUsuario findByUsuario(Usuario usuario);
}
