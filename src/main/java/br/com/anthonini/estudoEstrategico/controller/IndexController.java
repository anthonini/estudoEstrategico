package br.com.anthonini.estudoEstrategico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.anthonini.estudoEstrategico.config.AdminConfig;
import br.com.anthonini.estudoEstrategico.security.UsuarioSistema;

@Controller
public class IndexController {
	
	@Autowired
	private AdminConfig adminConfig;

	@GetMapping("/")
	public String index(@AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		if(usuarioSistema.equals(adminConfig.usuarioAdmin())) {
			return "redirect:/migracoes-banco";
		}
		return "redirect:/estudos";
	}
}
