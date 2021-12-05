package br.com.anthonini.estudoEstrategico.util;

import java.text.DecimalFormat;

public class PorcentagemUtil {

	public static String getPorcentagem(Integer quantidade, Integer total) {
		if(quantidade != null && quantidade >= 0 && total != null && total > 0) {
			DecimalFormat numberFormat = new DecimalFormat("#0.00");
			return numberFormat.format( ((double)quantidade/total)*100 ) + "%";
		}
		
		return "--";
	}
}
