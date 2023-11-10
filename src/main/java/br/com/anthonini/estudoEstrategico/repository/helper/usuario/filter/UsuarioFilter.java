package br.com.anthonini.estudoEstrategico.repository.helper.usuario.filter;

import br.com.anthonini.estudoEstrategico.model.Tema;

public class UsuarioFilter {

	private String nome;
	private String cpf;
	private String email;
	private Tema tema;
	private Boolean ativo;
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Tema getTema() {
		return tema;
	}
	public void setTema(Tema tema) {
		this.tema = tema;
	}
	public Boolean getAtivo() {
		return ativo;
	}
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
}
