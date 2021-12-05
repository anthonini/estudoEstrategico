package br.com.anthonini.estudoEstrategico.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.anthonini.estudoEstrategico.util.PorcentagemUtil;

@Entity
@Table(name = "disciplina_dia_estudo")
public class DisciplinaDiaEstudo implements Entidade {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_disciplina_dia_estudo")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_dia_estudo")
	private DiaEstudo diaEstudo;
	
	private Integer ordem;
	
	@ManyToOne
	@JoinColumn(name = "id_disciplina")
	private Disciplina disciplina;

	@Column(name = "carga_horaria")
	private Integer cargaHoraria;
	
	@Column(name = "carga_horaria_liquida")
	private Integer cargaHorariaLiquida;
	
	private Integer aula;
	
	@Column(name = "paginas_estudadas")
	private Integer paginasEstudadas;
	
	@Column(name = "pagina_inicial")
	private Integer paginaInicial;
	
	@Column(name = "pagina_final")
	private Integer paginaFinal;
	
	@Column(name = "quantidade_questoes_resolvidas")
	private Integer quantidadeQuestoesResolvidas;

	@Column(name = "quantidade_questoes_resolvidas_corretas")
	private Integer quantidadeQuestoesResolvidasCorretas;
	
	private String observacao;

	public String getPorcentagemAcertoQuestoes() {
		return PorcentagemUtil.getPorcentagem(quantidadeQuestoesResolvidasCorretas, quantidadeQuestoesResolvidas);
	}
	
	public boolean isEstudoIniciado() {
		return cargaHorariaLiquida != null || aula != null || paginaInicial != null || paginaFinal != null || quantidadeQuestoesResolvidas != null || quantidadeQuestoesResolvidasCorretas != null
				|| observacao != null;
	}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public DiaEstudo getDiaEstudo() {
		return diaEstudo;
	}


	public void setDiaEstudo(DiaEstudo diaEstudo) {
		this.diaEstudo = diaEstudo;
	}


	public Integer getOrdem() {
		return ordem;
	}


	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}


	public Disciplina getDisciplina() {
		return disciplina;
	}


	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
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
		DisciplinaDiaEstudo other = (DisciplinaDiaEstudo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
