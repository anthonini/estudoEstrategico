package br.com.anthonini.estudoEstrategico.model;

import java.util.List;

import br.com.anthonini.estudoEstrategico.util.CargaHorariaUtil;

public enum TipoRevisao {
	VINTE_QUATRO_HORAS(1, "24 Horas"),
	SETE_DIAS(7, "7 Dias"),
	TRINTA_DIAS(30, "30 Dias");
	
	private Integer quantidadeDias;
	private String descricao;

	private TipoRevisao(Integer quantidadeDias, String descricao) {
		this.quantidadeDias = quantidadeDias;
		this.descricao = descricao;
	}

	public Integer getQuantidadeDias() {
		return quantidadeDias;
	}

	public String getDescricao() {
		return descricao;
	}

	public Revisao getRevisao(List<DiaEstudo> diasEstudo, DiaEstudo diaEstudoAtual) {
		Revisao revisao = null;
		if(diaEstudoAtual.getDia() > this.getQuantidadeDias()) {
			revisao = new Revisao();
			revisao.setTipoRevisao(this);
			revisao.setDiaEstudo(diaEstudoAtual);
			revisao.setDiaEstudoRevisao(diasEstudo.get(diaEstudoAtual.getDia()-this.getQuantidadeDias()-1));
			revisao.setCargaHoraria(CargaHorariaUtil.getCargaHorariaRevisao(revisao.getDiaEstudoRevisao().getCargaHorariaDia()));

			return revisao;
		}
		return revisao;
	}
}
