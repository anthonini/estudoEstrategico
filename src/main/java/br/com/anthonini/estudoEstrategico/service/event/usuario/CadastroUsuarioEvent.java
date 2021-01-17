package br.com.anthonini.estudoEstrategico.service.event.usuario;

import br.com.anthonini.estudoEstrategico.model.Usuario;

public class CadastroUsuarioEvent {

	private Usuario usuario;

	public CadastroUsuarioEvent(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
