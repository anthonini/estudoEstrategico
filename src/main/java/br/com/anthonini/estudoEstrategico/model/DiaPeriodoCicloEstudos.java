package br.com.anthonini.estudoEstrategico.model;

import java.util.List;

public enum DiaPeriodoCicloEstudos {
	PRIMEIRO("Primeiro") {
		@Override
		public List<DisciplinaPeriodo> getDisciplinas(PeriodoCicloEstudos periodoCicloEstudos) {
			return periodoCicloEstudos.getDisciplinasPrimeiroDia();
		}

		@Override
		public void removerDisciplina(PeriodoCicloEstudos periodoCicloEstudos, Integer index) {
			DisciplinaPeriodo disciplina = periodoCicloEstudos.getDisciplinasPrimeiroDia().get(index);
			periodoCicloEstudos.getDisciplinas().removeIf(d -> d == disciplina);
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
			periodoCicloEstudos.getDisciplinas().removeIf(d -> d == disciplina);
		}
	};
	
	private String descricao;
	
	private DiaPeriodoCicloEstudos(String descricao) {
		this.descricao = descricao;
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
