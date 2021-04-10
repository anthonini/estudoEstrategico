package br.com.anthonini.estudoEstrategico.service.exception;

public class UsuarioSemPermissaoParaRealizarEssaOperacao extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UsuarioSemPermissaoParaRealizarEssaOperacao() {
		super("Você não possui permissão para realizar essa operação.");
	}
}
