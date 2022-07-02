package br.com.anthonini.estudoEstrategico.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

public class DashboardController {
	
	@GetMapping
	public ModelAndView index() {
		ModelAndView mv = new ModelAndView("dashboard");		
		return mv;
	}
}
