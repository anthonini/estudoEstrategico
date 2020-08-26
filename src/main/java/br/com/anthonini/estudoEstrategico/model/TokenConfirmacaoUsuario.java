package br.com.anthonini.estudoEstrategico.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "token_confirmacao_usuario")
public class TokenConfirmacaoUsuario implements Entidade {

	private static final long serialVersionUID = 1L;
	
	public static final int TEMPO_EXPIRACAO = 60 * 24;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_token_confirmacao_usuario")
	private Long id;
	
	 private String token;
	 
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "id_usuario")
	private Usuario usuario;

	@Column(name = "data_expiracao")
	private LocalDateTime dataExpiracao;

	private LocalDateTime calcularDataExpiracao(int tempoExpiracaoEmMinutos) {
		LocalDateTime dataHoraAtual = LocalDateTime.now();
		return dataHoraAtual.plus(tempoExpiracaoEmMinutos, ChronoUnit.MINUTES);
	}
	
	@PrePersist
	private void prePersist() {
		this.dataExpiracao = calcularDataExpiracao(TEMPO_EXPIRACAO);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public LocalDateTime getDataExpiracao() {
		return dataExpiracao;
	}

	public void setDataExpiracao(LocalDateTime dataExpiracao) {
		this.dataExpiracao = dataExpiracao;
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
		TokenConfirmacaoUsuario other = (TokenConfirmacaoUsuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
