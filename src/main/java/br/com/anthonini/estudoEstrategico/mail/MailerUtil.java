package br.com.anthonini.estudoEstrategico.mail;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

@Component
public class MailerUtil {

	@Autowired 
	private HttpServletRequest request;
	
	@Autowired
	private LocaleResolver localeResolver;
	
	public String getUrlServidor() {
		String endereco = request.getServerName();
		String contexto = request.getContextPath();
		int porta = request.getServerPort();
		
		return "http://" + endereco + (porta == 80 ? "" : ":"+porta) + contexto;
	}
	
	public Locale getLocale() {
		return localeResolver.resolveLocale(request);
	}
}
