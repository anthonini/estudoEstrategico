package br.com.anthonini.estudoEstrategico.service.exception;

public class NaoEPossivelRemoverEntidadeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NaoEPossivelRemoverEntidadeException(String message) {
		super(message);
	}
}
