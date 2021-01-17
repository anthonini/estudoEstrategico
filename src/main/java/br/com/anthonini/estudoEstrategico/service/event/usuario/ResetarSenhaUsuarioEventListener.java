package br.com.anthonini.estudoEstrategico.service.event.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.com.anthonini.estudoEstrategico.mail.RecuperarSenhaUsuarioMailer;
import br.com.anthonini.estudoEstrategico.model.TokenResetarSenhaUsuario;
import br.com.anthonini.estudoEstrategico.model.Usuario;
import br.com.anthonini.estudoEstrategico.service.TokenResetarSenhaUsuarioService;

@Component
public class ResetarSenhaUsuarioEventListener {
	
	@Autowired
	private TokenResetarSenhaUsuarioService tokenResetarSenhaUsuarioService;
	
	@Autowired
	private RecuperarSenhaUsuarioMailer mailer;

	@EventListener
	public void alteracaoSenhaUsuarioSolicitada(ResetarSenhaUsuarioEvent event) {
		Usuario usuario = event.getUsuario();
		TokenResetarSenhaUsuario token = tokenResetarSenhaUsuarioService.cadastrarToken(usuario);
		mailer.enviarEmailResetarSenha(usuario, token.getToken());
	}
}
