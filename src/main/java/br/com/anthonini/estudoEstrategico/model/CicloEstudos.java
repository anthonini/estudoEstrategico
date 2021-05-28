package br.com.anthonini.estudoEstrategico.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "ciclo_estudos")
public class CicloEstudos implements Entidade {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_ciclo_estudos")
	private Long id;
	
	@NotBlank(message = "Nome é obrigatório")
	private String nome;
	
	@ManyToOne
	@JoinColumn(name = "id_usuario", nullable = false)
	private Usuario usuario;
	
	@Size(min = 1, message = "É obrigatório adicionar no mínimo um período")
	//@OneToMany(mappedBy = "cicloEstudos", cascade = CascadeType.ALL)
	//@OrderBy("ordem")
	@Transient
	private List<PeriodoCicloEstudos> periodosCicloEstudos = new ArrayList<>();
	
	@Transient
	private String uuid;
	
	public void adicionarPeriodo(PeriodoCicloEstudos periodoCicloEstudos) {
		periodosCicloEstudos.add(periodoCicloEstudos);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<PeriodoCicloEstudos> getPeriodosCicloEstudos() {
		return periodosCicloEstudos;
	}

	public void setPeriodosCicloEstudos(List<PeriodoCicloEstudos> periodosCicloEstudos) {
		this.periodosCicloEstudos = periodosCicloEstudos;
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
		CicloEstudos other = (CicloEstudos) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
