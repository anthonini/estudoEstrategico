package br.com.anthonini.estudoEstrategico.model;

import java.util.List;

public enum DiaPeriodoCicloEstudos {
	PRIMEIRO("Primeiro") {
		@Override
		public List<DisciplinaPeriodo> getDisciplinas(PeriodoCicloEstudos periodoCicloEstudos) {
			return periodoCicloEstudos.getDisciplinasPeriodoPrimeiroDia();
		}

		@Override
		public void adicionarDisciplina(PeriodoCicloEstudos periodoCicloEstudos, DisciplinaPeriodo disciplinaPeriodo) {
			periodoCicloEstudos.getDisciplinasPeriodoPrimeiroDia().add(disciplinaPeriodo);
		}

		@Override
		public void removerDisciplina(PeriodoCicloEstudos periodoCicloEstudos, Long idDisciplina) {
			periodoCicloEstudos.getDisciplinasPeriodoPrimeiroDia().removeIf(d -> d.getDisciplina().getId().equals(idDisciplina));
		}
	},
	SEGUNDO("Segundo") {
		@Override
		public List<DisciplinaPeriodo> getDisciplinas(PeriodoCicloEstudos periodoCicloEstudos) {
			return periodoCicloEstudos.getDisciplinasPeriodoSegundoDia();
		}

		@Override
		public void adicionarDisciplina(PeriodoCicloEstudos periodoCicloEstudos, DisciplinaPeriodo disciplinaPeriodo) {
			periodoCicloEstudos.getDisciplinasPeriodoSegundoDia().add(disciplinaPeriodo);
		}

		@Override
		public void removerDisciplina(PeriodoCicloEstudos periodoCicloEstudos, Long idDisciplina) {
			periodoCicloEstudos.getDisciplinasPeriodoSegundoDia().removeIf(d -> d.getDisciplina().getId().equals(idDisciplina));
		}
	};
	
	private String descricao;
	
	private DiaPeriodoCicloEstudos(String descricao) {
		this.descricao = descricao;
	}
	
	public abstract List<DisciplinaPeriodo> getDisciplinas(PeriodoCicloEstudos periodoCicloEstudos);
	
	public abstract void adicionarDisciplina(PeriodoCicloEstudos periodoCicloEstudos, DisciplinaPeriodo disciplinaPeriodo);
	
	public abstract void removerDisciplina(PeriodoCicloEstudos periodoCicloEstudos, Long idDIsciplina);

	public String getDescricao() {
		return descricao;
	}
	
	public String getMetodo() {
		return descricao.toLowerCase();
	}
}
