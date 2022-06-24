package br.com.anthonini.estudoEstrategico.repository.helper.cicloEstudos.filter;

import br.com.anthonini.estudoEstrategico.model.Usuario;

public class CicloEstudosFilter {

	private String nome;
	private Usuario usuario;
	private String nomeUsuario;
	private Usuario usuarioProfessor;
	
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

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public Usuario getUsuarioProfessor() {
		return usuarioProfessor;
	}

	public void setUsuarioProfessor(Usuario usuarioProfessor) {
		this.usuarioProfessor = usuarioProfessor;
	}
}
