package br.com.anthonini.estudoEstrategico.repository.helper.usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import br.com.anthonini.arquitetura.controller.page.PaginationUtil;
import br.com.anthonini.estudoEstrategico.model.Usuario;
import br.com.anthonini.estudoEstrategico.repository.helper.usuario.filter.UsuarioFilter;

public class UsuarioRepositoryImpl implements UsuarioRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginationUtil<Usuario> paginationUtil;
	
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
	
	@Override
	public Page<Usuario> filtrar(UsuarioFilter filter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Usuario> criteriaQuery = builder.createQuery(Usuario.class);
		Root<Usuario> usuario = criteriaQuery.from(Usuario.class);
		
		criteriaQuery.where(getWhere(filter, builder, usuario));
		
		TypedQuery<Usuario> query = paginationUtil.prepare(builder, criteriaQuery, usuario, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(filter));
	}
	
	private Long total(UsuarioFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<Usuario> usuario = criteriaQuery.from(Usuario.class);
		
		criteriaQuery.select(builder.count(usuario)).where(getWhere(filter, builder, usuario));
		
		return manager.createQuery(criteriaQuery).getSingleResult();
	}


	private Predicate[] getWhere(UsuarioFilter filter, CriteriaBuilder builder, Root<Usuario> usuario) {
		List<Predicate> where = new ArrayList<>();
		
		if(filter != null) {			
			if (!StringUtils.isEmpty(filter.getNome())) {
				where.add(builder.like(builder.upper(usuario.get("pessoa").get("nome")), "%"+filter.getNome().toUpperCase()+"%"));
			}
			
			if (!StringUtils.isEmpty(filter.getEmail())) {
				where.add(builder.like(builder.upper(usuario.get("email")), "%"+filter.getEmail().toUpperCase()+"%"));
			}
			
			if (filter.getAtivo() != null) {
				where.add(builder.equal(usuario.get("ativo"), filter.getAtivo()));
			}
			
			if (filter.getTema()!= null) {
				where.add(builder.equal(usuario.get("tema"), filter.getTema()));
			}
		}
		
		return where.stream().toArray(Predicate[]::new);
	}
}
