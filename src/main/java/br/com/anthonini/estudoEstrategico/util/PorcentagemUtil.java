package br.com.anthonini.estudoEstrategico.util;

public class PorcentagemUtil {

	public static String getPorcentagem(Integer quantidade, Integer total) {
		if(quantidade != null && quantidade > 0 && total != null && total > 0) {
			return ((double)total/quantidade)*100 + "%";
		}
		
		return "--";
	}
}
