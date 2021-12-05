package br.com.anthonini.estudoEstrategico.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import br.com.anthonini.estudoEstrategico.model.Usuario;

public class PerfilDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "Nome é obrigatório")
	private String nome;
	
	private String cpf;
	
	@NotBlank(message = "E-mail é obrigatório")
	@Email(message = "E-mail inválido")
	private String email;
	
	@NotBlank(message = "Senha é obrigatório")
	private String senha;
	
	public PerfilDTO() {}

	public PerfilDTO(Usuario usuario) {
		this.nome = usuario.getPessoa().getNome();
		this.cpf = usuario.getPessoa().getCpf();
		this.email = usuario.getEmail();
	}

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

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
}
