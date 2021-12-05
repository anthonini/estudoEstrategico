package br.com.anthonini.estudoEstrategico.repository.helper.disciplina;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.anthonini.estudoEstrategico.model.Disciplina;
import br.com.anthonini.estudoEstrategico.repository.helper.disciplina.filter.DisciplinaFilter;

public interface DisciplinaRepositoryQueries {

	public Page<Disciplina> filtrar(DisciplinaFilter filter, Pageable pageable);
}
