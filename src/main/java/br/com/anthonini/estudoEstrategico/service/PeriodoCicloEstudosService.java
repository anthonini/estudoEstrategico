package br.com.anthonini.estudoEstrategico.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import br.com.anthonini.estudoEstrategico.controller.validador.PeriodoCicloEstudosValidador;
import br.com.anthonini.estudoEstrategico.model.CicloEstudos;
import br.com.anthonini.estudoEstrategico.model.PeriodoCicloEstudos;
import br.com.anthonini.estudoEstrategico.service.exception.ErrosValidacaoException;

@Service
public class PeriodoCicloEstudosService {
	
	@Autowired
	private PeriodoCicloEstudosValidador validador;	
	
	public void adicionar(CicloEstudos cicloEstudos, PeriodoCicloEstudos periodoCicloEstudos, BindingResult bindingResult) {
		periodoCicloEstudos.setNumero(cicloEstudos.getPeriodosCicloEstudos().size()+1);
		validar(periodoCicloEstudos, bindingResult);		
		cicloEstudos.getPeriodosCicloEstudos().add(periodoCicloEstudos);
	}

	@Validated
	public void validar(PeriodoCicloEstudos periodoCicloEstudos, BindingResult bindingResult) {
		validador.validate(periodoCicloEstudos, bindingResult);
		if(bindingResult.hasErrors())
			throw new ErrosValidacaoException();
	}
	
	public void removerPeriodo(CicloEstudos cicloEstudos, int index) {
		if(index >= 0 && index < cicloEstudos.getPeriodosCicloEstudos().size()) {
			cicloEstudos.getPeriodosCicloEstudos().remove(index);
			for(int i = 0; i < cicloEstudos.getPeriodosCicloEstudos().size(); i++) {
				PeriodoCicloEstudos periodoCicloEstudos = cicloEstudos.getPeriodosCicloEstudos().get(i);
				periodoCicloEstudos.setNumero(i+1);
			}
		}
	}
}
