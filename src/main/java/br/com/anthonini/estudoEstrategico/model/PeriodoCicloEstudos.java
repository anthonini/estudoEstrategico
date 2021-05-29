package br.com.anthonini.estudoEstrategico.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Where;

@Entity
@Table(name = "periodo_ciclo_estudos")
public class PeriodoCicloEstudos implements Entidade  {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_periodo_ciclo_estudos")
	private Long id;
	
	@NotNull(message = "Duração é obrigatório")
	@Min(value = 1, message = "Duração deve ser maior ou igual à 1")
	private Integer duracao;
	
	@NotNull(message = "Número é obrigatório")
	@Column(nullable = false)
	private Integer numero;
	
	@NotNull(message = "Ciclo de Estudos é obrigatóroio")
	@ManyToOne
	@JoinColumn(name = "id_ciclo_estudos", nullable = false)
	private CicloEstudos cicloEstudos;
	
	@Size(min = 1, message = "É obrigatório adicionar no mínimo uma disciplina no primeiro dia")
	@OneToMany(mappedBy = "periodoCicloEstudos", cascade = CascadeType.ALL)
	@Where(clause = "dia = 'PRIMEIRO'")
	private List<DisciplinaPeriodo> disciplinasPeriodoPrimeiroDia = new ArrayList<>();
	
	@Size(min = 1, message = "É obrigatório adicionar no mínimo uma disciplina no segundo dia")
	@OneToMany(mappedBy = "periodoCicloEstudos", cascade = CascadeType.ALL)
	@Where(clause = "dia = 'SEGUNDO'")
	private List<DisciplinaPeriodo> disciplinasPeriodoSegundoDia = new ArrayList<>();
	
	public String getDescricaoDuracao() {
		return duracao + " dia" + (duracao > 1 ? "s" : "");
	}
	
	public String getDescricaoPrimeiroDia() {
		return disciplinasPeriodoPrimeiroDia.stream().map(DisciplinaPeriodo::getDisciplina).collect(Collectors.toList())
		.stream().map(Disciplina::getNome).collect(Collectors.joining(", "));
	}
	
	public String getDescricaoSegundoDia() {
		return disciplinasPeriodoSegundoDia.stream().map(DisciplinaPeriodo::getDisciplina).collect(Collectors.toList())
				.stream().map(Disciplina::getNome).collect(Collectors.joining(", "));
	}
	
	@Transient
	private String uuid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getDuracao() {
		return duracao;
	}

	public void setDuracao(Integer duracao) {
		this.duracao = duracao;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public CicloEstudos getCicloEstudos() {
		return cicloEstudos;
	}

	public void setCicloEstudos(CicloEstudos cicloEstudos) {
		this.cicloEstudos = cicloEstudos;
	}

	public List<DisciplinaPeriodo> getDisciplinasPeriodoPrimeiroDia() {
		return disciplinasPeriodoPrimeiroDia;
	}

	public void setDisciplinasPeriodoPrimeiroDia(List<DisciplinaPeriodo> disciplinasPeriodoPrimeiroDia) {
		this.disciplinasPeriodoPrimeiroDia = disciplinasPeriodoPrimeiroDia;
	}

	public List<DisciplinaPeriodo> getDisciplinasPeriodoSegundoDia() {
		return disciplinasPeriodoSegundoDia;
	}

	public void setDisciplinasPeriodoSegundoDia(List<DisciplinaPeriodo> disciplinasPeriodoSegundoDia) {
		this.disciplinasPeriodoSegundoDia = disciplinasPeriodoSegundoDia;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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
		PeriodoCicloEstudos other = (PeriodoCicloEstudos) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
