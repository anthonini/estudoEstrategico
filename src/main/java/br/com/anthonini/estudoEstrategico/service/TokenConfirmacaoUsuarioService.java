package br.com.anthonini.estudoEstrategico.service;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.anthonini.estudoEstrategico.model.TokenConfirmacaoUsuario;
import br.com.anthonini.estudoEstrategico.model.Usuario;
import br.com.anthonini.estudoEstrategico.repository.TokenConfirmacaoUsuarioRepository;

@Service
public class TokenConfirmacaoUsuarioService {
	
	@Autowired
	private TokenConfirmacaoUsuarioRepository repository;

	@Transactional
	public TokenConfirmacaoUsuario cadastrarToken(Usuario usuario) {
		TokenConfirmacaoUsuario token = new TokenConfirmacaoUsuario();
		token.setUsuario(usuario);
		token.setToken(UUID.randomUUID().toString());
		
		repository.save(token);
		
		return token;
	}
}
