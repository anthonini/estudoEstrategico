package br.com.anthonini.estudoEstrategico.service.exception;

public class SenhaNaoConfirmadaException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SenhaNaoConfirmadaException() {
		super("Senha não confirmada");
	}
}
