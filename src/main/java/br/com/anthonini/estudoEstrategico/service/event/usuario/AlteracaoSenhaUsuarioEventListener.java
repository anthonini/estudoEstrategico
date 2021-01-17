package br.com.anthonini.estudoEstrategico.service.event.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.com.anthonini.estudoEstrategico.mail.AlteracaoSenhaUsuarioMailer;
import br.com.anthonini.estudoEstrategico.model.Usuario;
import br.com.anthonini.estudoEstrategico.service.TokenResetarSenhaUsuarioService;

@Component
public class AlteracaoSenhaUsuarioEventListener {
	
	@Autowired
	private TokenResetarSenhaUsuarioService tokenResetarSenhaUsuarioService;
	
	@Autowired
	private AlteracaoSenhaUsuarioMailer mailer;

	@EventListener
	public void alteradoSenhaUsuario(AlteracaoSenhaUsuarioEvent event) {
		Usuario usuario = event.getUsuario();
		tokenResetarSenhaUsuarioService.inativarTokensAtivos(usuario);
		mailer.enviarEmailConfirmacaoAlteracaoSenha(usuario);
	}
}
