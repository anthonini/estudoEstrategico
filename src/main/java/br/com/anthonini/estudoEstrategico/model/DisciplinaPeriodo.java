package br.com.anthonini.estudoEstrategico.model;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

public class DisciplinaPeriodo implements Entidade {
	
	private static final long serialVersionUID = 1L;
	
	/*@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_disciplina_periodo")*/
	private Long id;
	
	@NotNull(message = "Disciplina é obrigatório")
	@ManyToOne
	@JoinColumn(name = "id_disciplina")
	private Disciplina disciplina;
	
	@NotNull(message = "Duração é obrigatório")
	private Integer duracao;
	
	@NotNull(message = "Dia é obrigatório")
	private Integer dia;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "id_periodo_ciclo_estudos")
	private PeriodoCicloEstudos periodoCicloEstudos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public Integer getDuracao() {
		return duracao;
	}

	public void setDuracao(Integer duracao) {
		this.duracao = duracao;
	}

	public Integer getDia() {
		return dia;
	}

	public void setDia(Integer dia) {
		this.dia = dia;
	}

	public PeriodoCicloEstudos getPeriodoCicloEstudos() {
		return periodoCicloEstudos;
	}

	public void setPeriodoCicloEstudos(PeriodoCicloEstudos periodoCicloEstudos) {
		this.periodoCicloEstudos = periodoCicloEstudos;
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
		DisciplinaPeriodo other = (DisciplinaPeriodo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
