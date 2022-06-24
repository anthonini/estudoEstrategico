package br.com.anthonini.estudoEstrategico.repository.helper.permissaousuario;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import br.com.anthonini.arquitetura.controller.page.PaginationUtil;
import br.com.anthonini.estudoEstrategico.model.GrupoUsuario;
import br.com.anthonini.estudoEstrategico.model.Permissao;
import br.com.anthonini.estudoEstrategico.model.Pessoa;
import br.com.anthonini.estudoEstrategico.model.Usuario;
import br.com.anthonini.estudoEstrategico.repository.helper.permissaousuario.filter.PermissaoUsuarioFilter;

public class PermissaoUsuarioRepositoryImpl implements PermissaoUsuarioRepositoryQueries {

	@PersistenceContext
	EntityManager manager;
	
	@Autowired
	private PaginationUtil<Usuario> paginationUtil;
	
	@Override
	public Page<Usuario> filtrar(PermissaoUsuarioFilter filter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Usuario> criteriaQuery = builder.createQuery(Usuario.class);
		Root<Usuario> usuario = criteriaQuery.from(Usuario.class);
		usuario.fetch("pessoa", JoinType.INNER);
		
		criteriaQuery.where(getWhere(filter, builder, usuario)).distinct(true);
		
		TypedQuery<Usuario> query = paginationUtil.prepare(builder, criteriaQuery, usuario, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(filter));
	}
	
	private Long total(PermissaoUsuarioFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<Usuario> usuario = criteriaQuery.from(Usuario.class);
		
		criteriaQuery.select(builder.count(usuario)).where(getWhere(filter, builder, usuario));
		
		return manager.createQuery(criteriaQuery).getSingleResult();
	}


	private Predicate[] getWhere(PermissaoUsuarioFilter filter, CriteriaBuilder builder, Root<Usuario> usuario) {
		List<Predicate> where = new ArrayList<>();
		
		if(filter != null) {			
			if (!StringUtils.isEmpty(filter.getNome())) {
				Join<Pessoa, Usuario> pessoa = usuario.join("pessoa", JoinType.INNER);
				where.add(builder.like(builder.upper(pessoa.get("nome")), "%"+filter.getNome().toUpperCase()+"%"));
			}
			
			if(filter.getGruposUsuario() != null && !filter.getGruposUsuario().isEmpty()) {
				for(GrupoUsuario grupo : filter.getGruposUsuario()) {
					where.add(usuario.join("grupos").in(grupo));
				}
			}
			
			if(filter.getPermissoes() != null && !filter.getPermissoes().isEmpty()) {
				for(Permissao permissao : filter.getPermissoes()) {
					where.add(usuario.join("grupos").join("permissoes").in(permissao));
				}
			}
		}
		
		return where.stream().toArray(Predicate[]::new);
	}

}
