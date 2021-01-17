package br.com.anthonini.estudoEstrategico.service.exception;

public class CPFJaCadastradoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CPFJaCadastradoException() {
		super("CPF já cadastrado");
	}
}
