package br.com.anthonini.estudoEstrategico.repository.helper.cicloEstudos.filter;

import br.com.anthonini.estudoEstrategico.model.Usuario;

public class CicloEstudosFilter {

	private String nome;
	private Usuario usuario;
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
