package br.com.anthonini.estudoEstrategico.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "dia_estudo")
public class DiaEstudo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_dia_estudo")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_ciclo_Estudos")
	private CicloEstudos cicloEstudos;

	private Integer dia;

	@OneToMany(mappedBy = "diaEstudo", cascade = CascadeType.ALL)
	@OrderBy("ordem")
	private List<DisciplinaDiaEstudo> disciplinas = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "diaEstudo")
	private List<Revisao> revisoes = new ArrayList<>();
	
	public int getQuantidadeItens() {
		return getDisciplinas().size() + getRevisoes().size();
	}
	
	public String getDiaFormatado() {
		return String.format("%03d", dia);
	}
	
	public Integer getCargaHorariaDia() {
		return disciplinas.stream().mapToInt(d -> d.getCargaHoraria()).sum();
	}
	
	public Integer getCargaHorariaRevisao() {
		return revisoes.stream().mapToInt(r -> r.getCargaHoraria()).sum();
	}
	
	public boolean isEstudoIniciado() {
		for(DisciplinaDiaEstudo disciplinaDiaEstudo : disciplinas) {
			if(disciplinaDiaEstudo.isEstudoIniciado())
				return true;
		}
		for(Revisao revisao : revisoes) {
			if(revisao.isEstudoIniciado())
				return true;
		}
		
		return false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CicloEstudos getCicloEstudos() {
		return cicloEstudos;
	}

	public void setCicloEstudos(CicloEstudos cicloEstudos) {
		this.cicloEstudos = cicloEstudos;
	}

	public Integer getDia() {
		return dia;
	}

	public void setDia(Integer dia) {
		this.dia = dia;
	}

	public List<DisciplinaDiaEstudo> getDisciplinas() {
		return disciplinas;
	}

	public void setDisciplinas(List<DisciplinaDiaEstudo> disciplinas) {
		this.disciplinas = disciplinas;
	}

	public List<Revisao> getRevisoes() {
		return revisoes;
	}

	public void setRevisoes(List<Revisao> revisoes) {
		this.revisoes = revisoes;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cicloEstudos, dia);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DiaEstudo other = (DiaEstudo) obj;
		return Objects.equals(cicloEstudos, other.cicloEstudos) && Objects.equals(dia, other.dia);
	}
}
