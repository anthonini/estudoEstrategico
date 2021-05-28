package br.com.anthonini.estudoEstrategico.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.annotation.Validated;

import br.com.anthonini.estudoEstrategico.model.CicloEstudos;
import br.com.anthonini.estudoEstrategico.model.PeriodoCicloEstudos;
import br.com.anthonini.estudoEstrategico.service.exception.ErrosValidacaoException;

@Service
public class PeriodoCicloEstudosService {
	
	@Autowired
	private SmartValidator validator;
	
	public void adicionar(CicloEstudos cicloEstudos, PeriodoCicloEstudos periodoCicloEstudos, BindingResult bindingResult) {
		periodoCicloEstudos.setNumero(cicloEstudos.getPeriodosCicloEstudos().size()+1);		
		validar(periodoCicloEstudos, bindingResult);		
		cicloEstudos.adicionarPeriodo(periodoCicloEstudos);
	}

	@Validated
	public void validar(PeriodoCicloEstudos periodoCicloEstudos, BindingResult bindingResult) {
		validator.validate(periodoCicloEstudos, bindingResult);
		if(bindingResult.hasErrors())
			throw new ErrosValidacaoException();
	}
}
