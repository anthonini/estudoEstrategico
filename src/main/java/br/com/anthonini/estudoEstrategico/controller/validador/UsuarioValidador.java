package br.com.anthonini.estudoEstrategico.controller.validador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.Validator;

import br.com.anthonini.estudoEstrategico.model.Usuario;

@Component
public class UsuarioValidador implements Validator {

	@Autowired
	private SmartValidator validator;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Usuario.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		validator.validate((Usuario)target, errors);
	}
}
