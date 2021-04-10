package br.com.anthonini.estudoEstrategico.repository.helper.disciplina;

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
import br.com.anthonini.estudoEstrategico.model.Disciplina;
import br.com.anthonini.estudoEstrategico.repository.helper.disciplina.filter.DisciplinaFilter;

public class DisciplinaRepositoryQueriesImpl implements DisciplinaRepositoryQueries {

	@PersistenceContext
	EntityManager manager;
	
	@Autowired
	private PaginationUtil<Disciplina> paginationUtil;
	
	@Override
	public Page<Disciplina> filtrar(DisciplinaFilter filter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Disciplina> criteriaQuery = builder.createQuery(Disciplina.class);
		Root<Disciplina> disciplina = criteriaQuery.from(Disciplina.class);		
		
		criteriaQuery.where(getWhere(filter, builder, disciplina)).distinct(true);
		
		TypedQuery<Disciplina> query = paginationUtil.prepare(builder, criteriaQuery, disciplina, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(filter));
	}
	
	private Long total(DisciplinaFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<Disciplina> feira = criteriaQuery.from(Disciplina.class);
		
		criteriaQuery.select(builder.count(feira)).where(getWhere(filter, builder, feira));
		
		return manager.createQuery(criteriaQuery).getSingleResult();
	}


	private Predicate[] getWhere(DisciplinaFilter filter, CriteriaBuilder builder, Root<Disciplina> feira) {
		List<Predicate> where = new ArrayList<>();
		
		if(filter != null) {			
			if (!StringUtils.isEmpty(filter.getNome())) {
				where.add(builder.like(builder.upper(feira.get("nome")), "%"+filter.getNome().toUpperCase()+"%"));
			}
		}
		
		return where.stream().toArray(Predicate[]::new);
	}

}
