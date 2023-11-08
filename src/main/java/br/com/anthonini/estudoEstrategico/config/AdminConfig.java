package br.com.anthonini.estudoEstrategico.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import br.com.anthonini.estudoEstrategico.model.Pessoa;
import br.com.anthonini.estudoEstrategico.model.Usuario;
import br.com.anthonini.estudoEstrategico.security.UsuarioSistema;

@Configuration
public class AdminConfig {

	@Value("${application.admin.email}")
	private String adminEmail;
	
	@Value("${application.admin.password}")
	private String adminPassword;
	
	public UsuarioSistema usuarioAdmin() {
		if(adminEmail != null && !adminEmail.isBlank() && adminPassword != null && !adminPassword.isBlank()) {
			Usuario usuario = new Usuario(-1L);
			usuario.setEmail(adminEmail);
			usuario.setSenha(adminPassword);
			usuario.setPessoa(new Pessoa());
			usuario.getPessoa().setNome("Administrador");
			
			return new UsuarioSistema(usuario, List.of(new SimpleGrantedAuthority("ROLE_REALIZAR_MIGRACOES")));
		}
		
		return null;
	}

	public String getAdminEmail() {
		return adminEmail;
	}
}
