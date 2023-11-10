package br.com.anthonini.estudoEstrategico.service.exception;

public class OperacaoDeveSerReiniciada extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OperacaoDeveSerReiniciada() {
		super("A operação deve ser reiniciada.");
	}
}
