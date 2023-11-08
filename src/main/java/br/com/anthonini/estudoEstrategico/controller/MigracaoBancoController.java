package br.com.anthonini.estudoEstrategico.controller;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.anthonini.arquitetura.controller.AbstractController;

@Controller
@RequestMapping("/migracao-banco")
public class MigracaoBancoController extends AbstractController {
	
	@Autowired
	private Flyway flyway;
	
	@GetMapping
	public String listar(ModelMap modelMap) {
		modelMap.addAttribute("migracoes", flyway.info().all());
		return "migracoes-banco/List";
	}

	@GetMapping("/migrar")
	public String iniciarMigracao(ModelMap modelMap) {
		return "migracoes-banco/Form";
	}
	
	@PostMapping("/migrar")
	public String migrar(ModelMap modelMap) {
		try {
			flyway.repair();
			flyway.migrate();
			addMensagemSucesso(modelMap, getMessage("migracao-banco.mensagem.sucesso"));
		} catch (Exception e) {
			addMensagemErro(modelMap, getMessage("migracao-banco.mensagem.erro"));
			modelMap.addAttribute("stackTrace", e.getStackTrace());
		}
		
		return iniciarMigracao(modelMap);
	}
}
