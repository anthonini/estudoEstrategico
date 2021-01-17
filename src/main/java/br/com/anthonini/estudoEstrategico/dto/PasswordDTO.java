package br.com.anthonini.estudoEstrategico.dto;

import javax.validation.constraints.NotBlank;

import br.com.anthonini.estudoEstrategico.validacao.AtributoConfirmacao;

@AtributoConfirmacao(atributo = "senha", atributoConfirmacao = "confirmacaoSenha", message = "Confirmação da senha não confere")
public class PasswordDTO {

	@NotBlank(message = "Senha é obrigatório")
	private String senha;
	
	@NotBlank(message = "Confirmação da Senha é obrigatório")
	private String confirmacaoSenha;

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getConfirmacaoSenha() {
		return confirmacaoSenha;
	}

	public void setConfirmacaoSenha(String confirmacaoSenha) {
		this.confirmacaoSenha = confirmacaoSenha;
	}
}
