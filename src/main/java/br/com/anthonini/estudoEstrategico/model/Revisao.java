package br.com.anthonini.estudoEstrategico.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.anthonini.estudoEstrategico.util.PorcentagemUtil;

@Entity
@Table(name = "revisao")
public class Revisao implements Entidade {

	private static final long serialVersionUID = 1L;	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_revisao")
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private TipoRevisao tipoRevisao;
	
	@ManyToOne
	@JoinColumn(name = "id_dia_estudo")
	private DiaEstudo diaEstudo;
	
	@ManyToOne
	@JoinColumn(name = "id_dia_estudo_revisao")
	private DiaEstudo diaEstudoRevisao;
	
	@Column(name = "carga_horaria")
	private Integer cargaHoraria;
	
	@Column(name = "carga_horaria_liquida")
	private Integer cargaHorariaLiquida;
	
	@Column(name = "quantidade_questoes_resolvidas")
	private Integer quantidadeQuestoesResolvidas;

	@Column(name = "quantidade_questoes_resolvidas_corretas")
	private Integer quantidadeQuestoesResolvidasCorretas;
	
	private String observacao;
	
	public String getDescricao() {
		return String.format("Revisão %03d", diaEstudoRevisao.getDia());
	}

	public String getPorcentagemAcertoQuestoes() {
		return PorcentagemUtil.getPorcentagem(quantidadeQuestoesResolvidasCorretas, quantidadeQuestoesResolvidas);
	}
	
	public boolean isEstudoIniciado() {
		return cargaHorariaLiquida != null || quantidadeQuestoesResolvidas != null || quantidadeQuestoesResolvidasCorretas != null || observacao != null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoRevisao getTipoRevisao() {
		return tipoRevisao;
	}

	public void setTipoRevisao(TipoRevisao tipoRevisao) {
		this.tipoRevisao = tipoRevisao;
	}

	public DiaEstudo getDiaEstudo() {
		return diaEstudo;
	}

	public void setDiaEstudo(DiaEstudo diaEstudo) {
		this.diaEstudo = diaEstudo;
	}

	public DiaEstudo getDiaEstudoRevisao() {
		return diaEstudoRevisao;
	}

	public void setDiaEstudoRevisao(DiaEstudo diaEstudoRevisao) {
		this.diaEstudoRevisao = diaEstudoRevisao;
	}

	public Integer getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(Integer cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Revisao other = (Revisao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
