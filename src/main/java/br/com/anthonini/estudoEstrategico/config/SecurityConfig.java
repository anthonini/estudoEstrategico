package br.com.anthonini.estudoEstrategico.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import br.com.anthonini.estudoEstrategico.security.AppUserDetailsService;

@EnableWebSecurity
@ComponentScan(basePackageClasses = AppUserDetailsService.class)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
			.antMatchers("/layout/**")
			.antMatchers("/h2-console/**")
			.antMatchers("/usuario/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(requests -> requests
                    .antMatchers("/grupo-usuario/**").hasRole("CADASTRAR_GRUPO_USUARIO")
                    .antMatchers("/permissao-usuario/**").hasRole("CADASTRAR_PERMISSOES")
                    .antMatchers("/professor/**").hasRole("PROFESSOR")
                    .antMatchers("/migracao-banco/**").hasRole("IMPLANTAR_MIGRACOES")
                    .anyRequest().authenticated())
            .formLogin(login -> login
                    .loginPage("/login")
                    .permitAll())
            .logout(logout -> logout
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout")))
            .sessionManagement(management -> management
                    .invalidSessionUrl("/login"));
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
