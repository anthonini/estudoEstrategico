package br.com.anthonini.estudoEstrategico.repository.helper.usuario;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.anthonini.estudoEstrategico.model.Usuario;

public class UsuarioRepositoryImpl implements UsuarioRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public Optional<Usuario> buscarAtivoPorEmail(String email) {
		return manager
				.createQuery("from Usuario where lower(email) = lower(:email) and ativo = true", Usuario.class)
				.setParameter("email", email).getResultList().stream().findFirst();
	}
	
	@Override
	public List<String> permissoes(Usuario usuario) {
		return manager.createQuery(
				"select p.nome from Usuario u inner join u.grupos g inner join g.permissoes p where u = :user", String.class)
				.setParameter("user", usuario)
				.getResultList();
	}
}
