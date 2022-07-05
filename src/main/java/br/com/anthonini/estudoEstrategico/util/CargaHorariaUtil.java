package br.com.anthonini.estudoEstrategico.util;

import java.time.Duration;

public class CargaHorariaUtil {

	public static final double TEMPO_REVISAO_POR_HORA_ESTUDADA_EM_MINUTOS = 10;
	
	public static Integer getCargaHorariaDisciplina(Integer cargaHorariaDisciplina, Integer cargaHorariaRevisaoPorDisicplina) {
		return ajustarCargaHorariaMultiploCinco(cargaHorariaDisciplina - cargaHorariaRevisaoPorDisicplina);
	}
	
	public static Integer getCargaHorariaRevisao(Integer cargaHorariaDia) {
		int cargaHorariaRevisao = (int) ( cargaHorariaDia * TEMPO_REVISAO_POR_HORA_ESTUDADA_EM_MINUTOS/Duration.ofHours(1).toMinutes() );
		return ajustarCargaHorariaMultiploCinco(cargaHorariaRevisao);
	}
	
	private static Integer ajustarCargaHorariaMultiploCinco(Integer cargaHoraria) {
		return ((Long)(5*(Math.round(cargaHoraria/5.0)))).intValue();
	}
}
