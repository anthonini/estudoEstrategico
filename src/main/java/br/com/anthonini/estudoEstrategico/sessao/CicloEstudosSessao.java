package br.com.anthonini.estudoEstrategico.sessao;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.annotation.SessionScope;

import br.com.anthonini.estudoEstrategico.model.CicloEstudos;

@Component
@SessionScope
public class CicloEstudosSessao {

	private Map<String, CicloEstudos> ciclosEstudos = new HashMap<>();

    public CicloEstudos getCicloEstudos(String uuid) {
        return ciclosEstudos.get(uuid);
    }

    public void adicionar(CicloEstudos cicloEstudos) {
        if(StringUtils.isEmpty(cicloEstudos.getUuid())) {
        	cicloEstudos.setUuid(UUID.randomUUID().toString());
        }
        this.ciclosEstudos.put(cicloEstudos.getUuid(), cicloEstudos);
    }

    public void remover(String uuid) {
    	this.ciclosEstudos.remove(uuid);
    }
}
