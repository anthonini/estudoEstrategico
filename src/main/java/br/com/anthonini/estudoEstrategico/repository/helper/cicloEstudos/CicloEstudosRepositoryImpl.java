package br.com.anthonini.estudoEstrategico.repository.helper.cicloEstudos;

import java.util.ArrayList;
import java.util.List;

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
import br.com.anthonini.estudoEstrategico.model.CicloEstudos;
import br.com.anthonini.estudoEstrategico.repository.helper.cicloEstudos.filter.CicloEstudosFilter;

public class CicloEstudosRepositoryImpl implements CicloEstudosRepositoryQueries {

	@PersistenceContext
	EntityManager manager;
	
	@Autowired
	private PaginationUtil<CicloEstudos> paginationUtil;
	
	@Override
	public Page<CicloEstudos> filtrar(CicloEstudosFilter filter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<CicloEstudos> criteriaQuery = builder.createQuery(CicloEstudos.class);
		Root<CicloEstudos> cicloEstudos = criteriaQuery.from(CicloEstudos.class);		
		
		criteriaQuery.where(getWhere(filter, builder, cicloEstudos)).distinct(true);
		
		TypedQuery<CicloEstudos> query = paginationUtil.prepare(builder, criteriaQuery, cicloEstudos, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(filter));
	}
	
	private Long total(CicloEstudosFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<CicloEstudos> feira = criteriaQuery.from(CicloEstudos.class);
		
		criteriaQuery.select(builder.count(feira)).where(getWhere(filter, builder, feira));
		
		return manager.createQuery(criteriaQuery).getSingleResult();
	}


	private Predicate[] getWhere(CicloEstudosFilter filter, CriteriaBuilder builder, Root<CicloEstudos> cicloEstudos) {
		List<Predicate> where = new ArrayList<>();
		
		if(filter != null) {			
			if (!StringUtils.isEmpty(filter.getNome())) {
				where.add(builder.like(builder.upper(cicloEstudos.get("nome")), "%"+filter.getNome().toUpperCase()+"%"));
			}
			
			if(filter.getUsuario() != null) {
				where.add(builder.equal(cicloEstudos.get("usuario"), filter.getUsuario()));
			}
		}
		
		return where.stream().toArray(Predicate[]::new);
	}

}
