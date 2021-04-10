package br.com.anthonini.estudoEstrategico.service.exception;

public class NomeDisciplinaJaCadastradaException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NomeDisciplinaJaCadastradaException() {
		super("Já existe uma disciplina cadastrada com esse nome.");
	}
}
