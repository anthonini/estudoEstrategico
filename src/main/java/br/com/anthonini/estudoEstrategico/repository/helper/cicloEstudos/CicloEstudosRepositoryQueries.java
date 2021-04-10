package br.com.anthonini.estudoEstrategico.repository.helper.cicloEstudos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.anthonini.estudoEstrategico.model.CicloEstudos;
import br.com.anthonini.estudoEstrategico.repository.helper.cicloEstudos.filter.CicloEstudosFilter;

public interface CicloEstudosRepositoryQueries {

	public Page<CicloEstudos> filtrar(CicloEstudosFilter filter, Pageable pageable);
}
