package br.com.anthonini.estudoEstrategico.repository.helper.permissaousuario.filter;

import java.util.List;

import br.com.anthonini.estudoEstrategico.model.GrupoUsuario;
import br.com.anthonini.estudoEstrategico.model.Permissao;

public class PermissaoUsuarioFilter {

	private String nome;
	private List<GrupoUsuario> gruposUsuario;
	private List<Permissao> permissoes;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<GrupoUsuario> getGruposUsuario() {
		return gruposUsuario;
	}

	public void setGruposUsuario(List<GrupoUsuario> gruposUsuario) {
		this.gruposUsuario = gruposUsuario;
	}

	public List<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}
}
