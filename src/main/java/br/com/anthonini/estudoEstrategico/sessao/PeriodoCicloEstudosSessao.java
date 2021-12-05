package br.com.anthonini.estudoEstrategico.sessao;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.annotation.SessionScope;

import br.com.anthonini.estudoEstrategico.model.PeriodoCicloEstudos;

@Component
@SessionScope
public class PeriodoCicloEstudosSessao {

	private Map<String, PeriodoCicloEstudos> periodosCiclosEstudos = new HashMap<>();

    public PeriodoCicloEstudos getPeriodoCicloEstudos(String uuid) {
        return periodosCiclosEstudos.get(uuid);
    }

    public void adicionar(PeriodoCicloEstudos periodoCicloEstudos) {
        if(StringUtils.isEmpty(periodoCicloEstudos.getUuid())) {
        	periodoCicloEstudos.setUuid(UUID.randomUUID().toString());
        }
        this.periodosCiclosEstudos.put(periodoCicloEstudos.getUuid(), periodoCicloEstudos);
    }

    public void remover(String uuid) {
    	this.periodosCiclosEstudos.remove(uuid);
    }
}
