package br.com.anthonini.estudoEstrategico.controller.validador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.Validator;

import br.com.anthonini.estudoEstrategico.model.PeriodoCicloEstudos;

@Component
public class PeriodoCicloEstudosValidador implements Validator  {
	
	@Autowired
	private SmartValidator validator;

	@Override
	public boolean supports(Class<?> clazz) {
		return PeriodoCicloEstudos.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		PeriodoCicloEstudos periodoCicloEstudos = (PeriodoCicloEstudos)target;
		
		validator.validate(periodoCicloEstudos, errors);
		
		if(periodoCicloEstudos.getDisciplinasPrimeiroDia().isEmpty()) {
			errors.rejectValue("disciplinas", "", "É obrigatório adicionar no mínimo uma disciplina no primeiro dia");
		}
		if(periodoCicloEstudos.getDisciplinasSegundoDia().isEmpty()) {
			errors.rejectValue("disciplinas", "", "É obrigatório adicionar no mínimo uma disciplina no segundo dia");
		}
	}

}
