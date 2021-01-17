package br.com.anthonini.estudoEstrategico.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.anthonini.estudoEstrategico.model.TokenResetarSenhaUsuario;
import br.com.anthonini.estudoEstrategico.model.Usuario;
import br.com.anthonini.estudoEstrategico.repository.TokenResetarSenhaUsuarioRepository;

@Service
public class TokenResetarSenhaUsuarioService {

	@Autowired
	private TokenResetarSenhaUsuarioRepository repository;
	
	@Transactional
	public TokenResetarSenhaUsuario cadastrarToken(Usuario usuario) {
		inativarTokensAtivos(usuario);
		
		TokenResetarSenhaUsuario token = new TokenResetarSenhaUsuario();
		token.setUsuario(usuario);
		token.setToken(UUID.randomUUID().toString());
		
		repository.save(token);
		
		return token;
	}

	public TokenResetarSenhaUsuario getToken(String token) {
		return repository.findByToken(token);
	}

	public boolean tokenValido(String token) {
		TokenResetarSenhaUsuario tokenResetarSenha = getToken(token);

		return tokenResetarSenha != null && tokenResetarSenha.getDataExpiracao().isAfter(LocalDateTime.now()) 
				&& tokenResetarSenha.isAtivo();
	}
	
	public void inativarTokensAtivos(Usuario usuario) {
		List<TokenResetarSenhaUsuario> tokensAtivos = repository.findByAtivoTrueAndUsuario(usuario);
		tokensAtivos.stream().forEach(t -> t.setAtivo(false));
	}
}
