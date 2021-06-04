package br.com.anthonini.estudoEstrategico.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "disciplina_periodo")
public class DisciplinaPeriodo implements Entidade {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_disciplina_periodo")
	private Long id;
	
	@NotNull(message = "Disciplina é obrigatório")
	@ManyToOne
	@JoinColumn(name = "id_disciplina")
	private Disciplina disciplina;
	
	@NotNull(message = "Tempo é obrigatório")
	private Integer tempo;
	
	@Enumerated(EnumType.STRING)
	private DiaPeriodoCicloEstudos dia;
	
	private Integer ordem;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_periodo_ciclo_estudos")
	private PeriodoCicloEstudos periodoCicloEstudos;
	
	public String getDescricaoTempo() {
		return tempo + " minuto" + (tempo > 1 ? "s" : "");
	}

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

	public Integer getTempo() {
		return tempo;
	}

	public void setTempo(Integer tempo) {
		this.tempo = tempo;
	}

	public DiaPeriodoCicloEstudos getDia() {
		return dia;
	}

	public void setDia(DiaPeriodoCicloEstudos dia) {
		this.dia = dia;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
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
