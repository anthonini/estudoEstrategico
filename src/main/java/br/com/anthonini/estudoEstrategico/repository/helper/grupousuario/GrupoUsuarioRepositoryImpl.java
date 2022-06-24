package br.com.anthonini.estudoEstrategico.repository.helper.grupousuario;

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
import br.com.anthonini.estudoEstrategico.repository.helper.grupousuario.filter.GrupoUsuarioFilter;

public class GrupoUsuarioRepositoryImpl implements GrupoUsuarioRepositoryQueries {

	@PersistenceContext
	EntityManager manager;
	
	@Autowired
	private PaginationUtil<GrupoUsuario> paginationUtil;
	
	@Override
	public Page<GrupoUsuario> filtrar(GrupoUsuarioFilter filter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<GrupoUsuario> criteriaQuery = builder.createQuery(GrupoUsuario.class);
		Root<GrupoUsuario> grupoUsuario = criteriaQuery.from(GrupoUsuario.class);		
		
		criteriaQuery.where(getWhere(filter, builder, grupoUsuario)).distinct(true);
		
		TypedQuery<GrupoUsuario> query = paginationUtil.prepare(builder, criteriaQuery, grupoUsuario, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(filter));
	}
	
	private Long total(GrupoUsuarioFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<GrupoUsuario> feira = criteriaQuery.from(GrupoUsuario.class);
		
		criteriaQuery.select(builder.count(feira)).where(getWhere(filter, builder, feira));
		
		return manager.createQuery(criteriaQuery).getSingleResult();
	}


	private Predicate[] getWhere(GrupoUsuarioFilter filter, CriteriaBuilder builder, Root<GrupoUsuario> grupoUsuario) {
		List<Predicate> where = new ArrayList<>();
		
		if(filter != null) {			
			if (!StringUtils.isEmpty(filter.getNome())) {
				where.add(builder.like(builder.upper(grupoUsuario.get("nome")), "%"+filter.getNome().toUpperCase()+"%"));
			}
			
			if(filter.getPermissao() != null) {
				Join<Permissao, GrupoUsuario> permissao = grupoUsuario.join("permissoes", JoinType.INNER);
				where.add(builder.like(builder.upper(permissao.get("nome")), "%" + filter.getPermissao().toUpperCase() + "%"));
			}
		}
		
		return where.stream().toArray(Predicate[]::new);
	}

}
