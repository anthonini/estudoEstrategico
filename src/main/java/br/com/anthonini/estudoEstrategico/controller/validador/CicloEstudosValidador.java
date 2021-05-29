package br.com.anthonini.estudoEstrategico.controller.validador;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.anthonini.estudoEstrategico.model.CicloEstudos;

@Component
public class CicloEstudosValidador implements Validator  {

	@Override
	public boolean supports(Class<?> clazz) {
		return CicloEstudos.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		CicloEstudos cicloEstudos = (CicloEstudos)target;
		if(cicloEstudos.getPeriodosCicloEstudos().isEmpty()) {
			errors.rejectValue("periodosCicloEstudos", "", "É obrigatório adicionar no mínimo um período");
		}
	}

}
