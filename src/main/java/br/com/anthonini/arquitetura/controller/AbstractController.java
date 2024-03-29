package br.com.anthonini.arquitetura.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.WebRequest;

import br.com.anthonini.estudoEstrategico.security.UsuarioSistema;

/**
 * @author Anthonini
 */
public class AbstractController implements Controller {

	@Autowired
	private WebRequest request;
	
	@Autowired
	private MessageSource messageSource;
	
	private Locale getLocale() {
		return request.getLocale();
	}
	
	protected String getMessage(String message, Object... parametros) {
		return messageSource.getMessage(message, parametros, getLocale());
	}
	
	public UsuarioSistema getUsuarioLogado() {
		return (UsuarioSistema) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
