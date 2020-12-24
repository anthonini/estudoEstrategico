package br.com.anthonini.estudoEstrategico.service.event.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.com.anthonini.estudoEstrategico.mail.CadastroUsuarioMailer;
import br.com.anthonini.estudoEstrategico.model.TokenConfirmacaoUsuario;
import br.com.anthonini.estudoEstrategico.model.Usuario;
import br.com.anthonini.estudoEstrategico.service.TokenConfirmacaoUsuarioService;

@Component
public class ReenvioEmailConfirmacaoEventListener {
	
	@Autowired
	private TokenConfirmacaoUsuarioService tokenConfirmacaoUsuarioService;
	
	@Autowired
	private CadastroUsuarioMailer mailer;

	@EventListener
	public void usuarioCadastrado(ReenvioEmailConfirmacaoEvent event) {
		Usuario usuario = event.getUsuario();
		TokenConfirmacaoUsuario token = tokenConfirmacaoUsuarioService.atualizarToken(usuario);
		mailer.enviarEmailConfirmacao(usuario, token.getToken());
	}
}
