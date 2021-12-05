package br.com.anthonini.estudoEstrategico.dto;

import br.com.anthonini.estudoEstrategico.util.PorcentagemUtil;

public class DisciplinaDiaEstudoDTO {

	private Long id;
	
	private Integer cargaHorariaLiquida;
	
	private Integer aula;
		
	private Integer paginasEstudadas;
	
	private Integer paginaInicial;
	
	private Integer paginaFinal;
	
	private Integer quantidadeQuestoesResolvidas;
	
	private Integer quantidadeQuestoesResolvidasCorretas;
	
	private String observacao;
	
	public String getPorcentagemAcertoQuestoes() {
		return PorcentagemUtil.getPorcentagem(quantidadeQuestoesResolvidasCorretas, quantidadeQuestoesResolvidas);
	}

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

	public Integer getAula() {
		return aula;
	}

	public void setAula(Integer aula) {
		this.aula = aula;
	}

	public Integer getPaginasEstudadas() {
		return paginasEstudadas;
	}

	public void setPaginasEstudadas(Integer paginasEstudadas) {
		this.paginasEstudadas = paginasEstudadas;
	}

	public Integer getPaginaInicial() {
		return paginaInicial;
	}

	public void setPaginaInicial(Integer paginaInicial) {
		this.paginaInicial = paginaInicial;
	}

	public Integer getPaginaFinal() {
		return paginaFinal;
	}

	public void setPaginaFinal(Integer paginaFinal) {
		this.paginaFinal = paginaFinal;
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
