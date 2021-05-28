package br.com.anthonini.estudoEstrategico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.anthonini.arquitetura.controller.AbstractController;
import br.com.anthonini.estudoEstrategico.model.CicloEstudos;
import br.com.anthonini.estudoEstrategico.model.DiaPeriodoCicloEstudos;
import br.com.anthonini.estudoEstrategico.model.PeriodoCicloEstudos;
import br.com.anthonini.estudoEstrategico.security.UsuarioSistema;
import br.com.anthonini.estudoEstrategico.sessao.CicloEstudosSessao;
import br.com.anthonini.estudoEstrategico.sessao.PeriodoCicloEstudosSessao;

@Controller
@RequestMapping("/periodo-ciclo-estudos")
public class PeriodoCicloEstudosController extends AbstractController {
	
	@Autowired
	private CicloEstudosSessao cicloEstudosSessao;
	
	@Autowired
	private PeriodoCicloEstudosSessao sessao;
	
	@GetMapping("/cadastro")
	public ModelAndView form(String cicloId, PeriodoCicloEstudos periodoCicloEstudos, ModelMap model, RedirectAttributes redirect) {
		CicloEstudos cicloEstudos = cicloEstudosSessao.getCicloEstudos(cicloId);
		if (cicloEstudos == null) {
        	addMensagemErro(redirect, getMessage("ciclo-estudos.mensagem.naoEncontrado"));
            return new ModelAndView("redirect:/ciclo-estudos");
        }
		
		model.addAttribute("primeiroDia", DiaPeriodoCicloEstudos.PRIMEIRO);
		model.addAttribute("segundoDia", DiaPeriodoCicloEstudos.SEGUNDO);
		model.addAttribute("cicloId", cicloId);
		
		sessao.adicionar(periodoCicloEstudos);
		
		return new ModelAndView("periodo-ciclo-estudos/Form");
	}
	
	@PostMapping("/cadastro")
	public ModelAndView cadastro(String cicloId, String uuid, ModelMap modelMap, RedirectAttributes redirect, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {		
		CicloEstudos cicloEstudos = cicloEstudosSessao.getCicloEstudos(cicloId);
		if (cicloEstudos == null) {
        	addMensagemErro(redirect, getMessage("ciclo-estudos.mensagem.naoEncontrado"));
            return new ModelAndView("redirect:/ciclo-estudos");
        }
		PeriodoCicloEstudos periodoCicloEstudos = sessao.getPeriodoCicloEstudos(uuid);
		if (periodoCicloEstudos == null) {
        	addMensagemErro(redirect, getMessage("periodo-ciclo-estudos.mensagem.sucesso"));
            return new ModelAndView("/ciclo-estudos/cadastro?id="+cicloId);
        }
		
		cicloEstudos.adicionarPeriodo(periodoCicloEstudos);
		sessao.remover(uuid);
		
		addMensagemSucesso(redirect, getMessage("periodo-ciclo-estudos.mensagem.sucesso"));
		return new ModelAndView("redirect:/periodo-ciclo-estudos/cadastro?cicloId="+cicloId);
	}
	
	@GetMapping("/disciplinas-periodo")
	public String disciplinasPeriodo(String uuid, DiaPeriodoCicloEstudos dia, ModelMap model) {
		model.addAttribute("disciplinasPeriodo", dia.getDisciplinas(sessao.getPeriodoCicloEstudos(uuid)));		
		return "/periodo-ciclo-estudos/fragments/disciplinas";
	}
}
