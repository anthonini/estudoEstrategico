package br.com.anthonini.estudoEstrategico.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
	
	@NotNull(message = "Ciclo de Estudos é obrigatório")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_ciclo_estudos", nullable = false)
	private CicloEstudos cicloEstudos;
	
	@OneToMany(mappedBy = "periodoCicloEstudos", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<DisciplinaPeriodo> disciplinas = new ArrayList<>();
	
	public String getDescricaoDuracao() {
		return duracao + " dia" + (duracao > 1 ? "s" : "");
	}
	
	public List<DisciplinaPeriodo> getDisciplinasPrimeiroDia() {
		return disciplinas.stream().filter(dp -> dp.getDia().equals(DiaPeriodoCicloEstudos.PRIMEIRO)).collect(Collectors.toList());
	}
	
	public List<DisciplinaPeriodo> getDisciplinasSegundoDia() {
		return disciplinas.stream().filter(dp -> dp.getDia().equals(DiaPeriodoCicloEstudos.SEGUNDO)).collect(Collectors.toList());
	}
	
	public String getDescricaoPrimeiroDia() {
		return getDisciplinasPrimeiroDia().stream()
				.map(DisciplinaPeriodo::getDisciplina).collect(Collectors.toList())
				.stream().map(Disciplina::getNome).collect(Collectors.joining(", "));
	}
	
	public String getDescricaoSegundoDia() {
		return getDisciplinasSegundoDia().stream()
				.map(DisciplinaPeriodo::getDisciplina).collect(Collectors.toList())
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

	public List<DisciplinaPeriodo> getDisciplinas() {
		return disciplinas;
	}

	public void setDisciplinas(List<DisciplinaPeriodo> disciplinas) {
		this.disciplinas = disciplinas;
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
