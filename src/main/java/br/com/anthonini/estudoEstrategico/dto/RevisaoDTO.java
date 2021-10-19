package br.com.anthonini.estudoEstrategico.dto;

public class RevisaoDTO {

	private Long id;
	
	private Integer cargaHorariaLiquida;
	
	private Integer quantidadeQuestoesResolvidas;

	private Integer quantidadeQuestoesResolvidasCorretas;
	
	private String observacao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCargaHorariaLiquida() {
		return cargaHorariaLiquida;
	}

	public void setCargaHorariaLiquida(Integer cargaHorariaLiquida) {
		this.cargaHorariaLiquida = cargaHorariaLiquida;
	}

	public Integer getQuantidadeQuestoesResolvidas() {
		return quantidadeQuestoesResolvidas;
	}

	public void setQuantidadeQuestoesResolvidas(Integer quantidadeQuestoesResolvidas) {
		this.quantidadeQuestoesResolvidas = quantidadeQuestoesResolvidas;
	}

	public Integer getQuantidadeQuestoesResolvidasCorretas() {
		return quantidadeQuestoesResolvidasCorretas;
	}

	public void setQuantidadeQuestoesResolvidasCorretas(Integer quantidadeQuestoesResolvidasCorretas) {
		this.quantidadeQuestoesResolvidasCorretas = quantidadeQuestoesResolvidasCorretas;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
}
