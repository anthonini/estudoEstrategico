package br.com.anthonini.estudoEstrategico;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import br.com.anthonini.arquitetura.controller.SairController;
import br.com.anthonini.arquitetura.thymeleaf.ArquiteturaDialect;

@SpringBootApplication
@EnableJpaRepositories(enableDefaultTransactions = false)
public class EstudoEstrategicoApplication {

	public static void main(String[] args) {
		SpringApplication.run(EstudoEstrategicoApplication.class, args);
	}

	@Bean
	public LocaleResolver localeResolver() {
		return new FixedLocaleResolver(new Locale("pt", "BR"));
	}
	
	@Bean
	public ArquiteturaDialect arquiteturaDialect() {
		return new ArquiteturaDialect();
	}
	
	@Configuration
	@ComponentScan(basePackageClasses = { SairController.class })
	public static class MvcConfig implements WebMvcConfigurer {
	}
}
