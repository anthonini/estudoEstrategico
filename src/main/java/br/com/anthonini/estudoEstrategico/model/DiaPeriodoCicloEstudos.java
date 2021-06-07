package br.com.anthonini.estudoEstrategico.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum DiaPeriodoCicloEstudos {
	PRIMEIRO(1, "Primeiro"),
	SEGUNDO(2, "Segundo");
	
	private Integer dia;
	private String descricao;
	
	private DiaPeriodoCicloEstudos(Integer dia, String descricao) {
		this.dia = dia;
		this.descricao = descricao;
	}
	
	public List<DisciplinaPeriodo> getDisciplinas(PeriodoCicloEstudos periodoCicloEstudos) {
		return periodoCicloEstudos.getDisciplinas().stream()
				.filter(dp -> dp.getDia().equals(this))
				.collect(Collectors.toList());
	}
	
	public void removerDisciplina(PeriodoCicloEstudos periodoCicloEstudos, Integer index) {
		DisciplinaPeriodo disciplina = getDisciplinas(periodoCicloEstudos).get(index);
		periodoCicloEstudos.getDisciplinas().removeIf(d -> d == disciplina);
		reorganizarOrdemDisciplinas(periodoCicloEstudos.getDisciplinas());
	}
	
	public static DiaPeriodoCicloEstudos getDiaPeriodo(Integer dia) {
		int quantidadeDias = DiaPeriodoCicloEstudos.values().length;
		dia = dia > 0 && dia % quantidadeDias == 0 ? quantidadeDias : dia % quantidadeDias;
		
		for(DiaPeriodoCicloEstudos diaPeriodoCicloEstudos : DiaPeriodoCicloEstudos.values()) {
			if(diaPeriodoCicloEstudos.getDia().equals(dia))
				return diaPeriodoCicloEstudos;
		}
		
		return null;
	}

	public Integer getDia() {
		return dia;
	}

	public String getDescricao() {
		return descricao;
	}
	
	private void reorganizarOrdemDisciplinas(List<DisciplinaPeriodo> disciplinas) {
		Map<DiaPeriodoCicloEstudos, Integer> diaOrdem = new HashMap<>();
		for(DiaPeriodoCicloEstudos dia : DiaPeriodoCicloEstudos.values()) {
			diaOrdem.put(dia, 0);
		}
		
		for(DisciplinaPeriodo disciplina : disciplinas) {
			Integer ordem = diaOrdem.get(disciplina.getDia())+1;
			diaOrdem.put(disciplina.getDia(), ordem);
			disciplina.setOrdem(ordem);
		}
	}
}
