package br.com.anthonini.estudoEstrategico.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {

	@GetMapping("/login")
	public String login(@AuthenticationPrincipal User user) {
		if(user != null) {
			return "redirect:/dashboard";
		}
		
		return "Login";
	}
	
	@GetMapping("/403")
	public String acessDenied() {
		return "403";
	}
}
