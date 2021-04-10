package br.com.anthonini.estudoEstrategico.service.exception;

public class NomeEntidadeJaCadastradaException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NomeEntidadeJaCadastradaException(String message) {
		super(message);
	}
}
