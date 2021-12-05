package br.com.anthonini.estudoEstrategico.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@PropertySource(value = { "file:./.estudo_estrategico-mail.properties" }, ignoreResourceNotFound = true)
public class MailConfig {
	
	@Value("${estudo_estrategico.mail.host}")
	private String host;
	
	@Value("${estudo_estrategico.mail.port}")
	private int port;
	
	@Value("${estudo_estrategico.mail.username}")
	private String username;
	
	@Value("${estudo_estrategico.mail.password}")
	private String password;
	
	@Value("${estudo_estrategico.mail.from}")
	private String fromEmail;

	@Bean
	public JavaMailSender mailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setUsername(username);
		mailSender.setPassword(password);
		
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.debug", true);
		props.put("mail.smtp.connectiontimeout", 10000); // miliseconds

		mailSender.setJavaMailProperties(props);
		
		return mailSender;
	}

	public String getFromEmail() {
		return fromEmail;
	}
}
