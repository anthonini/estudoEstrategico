package br.com.anthonini.estudoEstrategico.mail;

import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import br.com.anthonini.estudoEstrategico.config.MailConfig;
import br.com.anthonini.estudoEstrategico.model.Usuario;

@Component
public class CadastroUsuarioMailer {
	
	private static Logger logger = LoggerFactory.getLogger(CadastroUsuarioMailer.class);

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private TemplateEngine thymeleaf;
	
	@Autowired
	private MailConfig mailConfig;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired 
	private HttpServletRequest request;
	
	@Autowired
	private LocaleResolver localeResolver;
	
	public void enviarEmailConfirmacao(Usuario usuario, String token) {
		Locale locale = localeResolver.resolveLocale(request);
		Context context = new Context(locale);
		context.setVariable("usuario", usuario);
		context.setVariable("seta", "seta");
		context.setVariable("linkConfirmacao", getUrlServidor()+"/usuario/confirmacao?token="+token);
		
		try {
			String email = thymeleaf.process("mail/confirmacao-cadastro-usuario", context);
			
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			helper.setFrom(mailConfig.getFromEmail());
			helper.setTo(usuario.getEmail());
			helper.setSubject(String.format("Confirmação de cadastro no %s", messageSource.getMessage("nome", null, locale)));
			helper.setText(email, true);
			helper.addInline("seta", new ClassPathResource("static/layout/images/seta.png"));
		
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			logger.error("Erro ao enviar e-mail", e);
		}
	}
	
	private String getUrlServidor() {
		String endereco = request.getServerName();
		String contexto = request.getContextPath();
		int porta = request.getServerPort();
		
		return "http://" + endereco + (porta == 80 ? "" : ":"+porta) + contexto;
	}
}
