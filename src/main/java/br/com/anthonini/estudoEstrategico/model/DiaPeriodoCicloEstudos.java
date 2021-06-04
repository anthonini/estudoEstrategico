package br.com.anthonini.estudoEstrategico.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum DiaPeriodoCicloEstudos {
	PRIMEIRO("Primeiro") {
		@Override
		public List<DisciplinaPeriodo> getDisciplinas(PeriodoCicloEstudos periodoCicloEstudos) {
			return periodoCicloEstudos.getDisciplinasPrimeiroDia();
		}

		@Override
		public void removerDisciplina(PeriodoCicloEstudos periodoCicloEstudos, Integer index) {
			DisciplinaPeriodo disciplina = periodoCicloEstudos.getDisciplinasPrimeiroDia().get(index);
			removerDisciplina(periodoCicloEstudos.getDisciplinas(), disciplina);
		}
	},
	SEGUNDO("Segundo") {
		@Override
		public List<DisciplinaPeriodo> getDisciplinas(PeriodoCicloEstudos periodoCicloEstudos) {
			return periodoCicloEstudos.getDisciplinasSegundoDia();
		}

		@Override
		public void removerDisciplina(PeriodoCicloEstudos periodoCicloEstudos, Integer index) {
			DisciplinaPeriodo disciplina = periodoCicloEstudos.getDisciplinasSegundoDia().get(index);
			removerDisciplina(periodoCicloEstudos.getDisciplinas(), disciplina);
		}
	};
	
	private String descricao;
	
	private DiaPeriodoCicloEstudos(String descricao) {
		this.descricao = descricao;
	}
	
	private static void removerDisciplina(List<DisciplinaPeriodo> disciplinas, DisciplinaPeriodo disciplina) {
		disciplinas.removeIf(d -> d == disciplina);
		reorganizarOrdem(disciplinas);
	}
	
	private static void reorganizarOrdem(List<DisciplinaPeriodo> disciplinas) {
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
	
	public abstract List<DisciplinaPeriodo> getDisciplinas(PeriodoCicloEstudos periodoCicloEstudos);
	
	public abstract void removerDisciplina(PeriodoCicloEstudos periodoCicloEstudos, Integer index);

	public String getDescricao() {
		return descricao;
	}
	
	public String getMetodo() {
		return descricao.toLowerCase();
	}
}
