package br.com.anthonini.arquitetura.controller.page;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PaginationUtil<T> {
	
	@PersistenceContext
	private EntityManager manager;

	public TypedQuery<T> prepare(CriteriaBuilder builder, CriteriaQuery<T> criteriaQuery, Root<T> root, Pageable pageable) {
		Sort sort = pageable.getSort();
		if (sort != null && !sort.isEmpty()) {
			Sort.Order order = sort.iterator().next();
			criteriaQuery.orderBy(order.isAscending() ? builder.asc(getPath(order, root)) : builder.desc(getPath(order, root)));
		}
		
		TypedQuery<T> query =  manager.createQuery(criteriaQuery);
		query.setFirstResult(pageable.getPageNumber()*pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
		
		return query;
	}
	
	private Path<T> getPath(Sort.Order order, Root<T> root) {
		String property = order.getProperty();
		String[] attributes = property.split("\\.");
		
		Path<T> path = null;
		for (String attribute : attributes) {
			path = path == null ? root.get(attribute) : path.get(attribute);
		}
		
		return path;
	}
}
