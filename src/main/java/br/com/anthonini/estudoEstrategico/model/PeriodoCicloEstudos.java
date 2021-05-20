package br.com.anthonini.estudoEstrategico.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Where;

public class PeriodoCicloEstudos implements Entidade  {

	private static final long serialVersionUID = 1L;
	
	/*@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_periodo_ciclo_estudos")*/
	private Long id;
	
	@NotNull(message = "Período é obrigatório")
	@Min(value = 1)
	private Integer periodo;
	
	@NotNull(message = "Ordem é obrigatório")
	@Min(value = 1)
	private Integer ordem;
	
	@NotNull(message = "Ciclo de estudos é obrigatório")
	@ManyToOne
	@JoinColumn(name = "id_ciclo_estudos")
	private CicloEstudos cicloEstudos;
	
	@Size(min = 1, message = "É obrigatório adicionar no mínimo uma disciplina no primeiro dia")
	@OneToMany(mappedBy = "periodoCicloEstudos", cascade = CascadeType.ALL)
	@Where(clause = "dia = 1")
	private List<DisciplinaPeriodo> disciplinasPeriodoPrimeiroDia = new ArrayList<>();
	
	@Size(min = 1, message = "É obrigatório adicionar no mínimo uma disciplina no segundo dia")
	@OneToMany(mappedBy = "periodoCicloEstudos", cascade = CascadeType.ALL)
	@Where(clause = "dia = 2")
	private List<DisciplinaPeriodo> disciplinasPeriodoSegundoDia = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
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
