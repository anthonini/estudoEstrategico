package br.com.anthonini.estudoEstrategico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.anthonini.arquitetura.controller.AbstractController;
import br.com.anthonini.estudoEstrategico.model.CicloEstudos;
import br.com.anthonini.estudoEstrategico.model.Disciplina;
import br.com.anthonini.estudoEstrategico.model.DisciplinaPeriodo;
import br.com.anthonini.estudoEstrategico.model.PeriodoCicloEstudos;
import br.com.anthonini.estudoEstrategico.security.UsuarioSistema;
import br.com.anthonini.estudoEstrategico.service.DisciplinaService;
import br.com.anthonini.estudoEstrategico.sessao.CicloEstudosSessao;

@Controller
@RequestMapping("/periodo-ciclo-estudos")
public class PeriodoCicloEstudosController extends AbstractController {
	
	@Autowired
	private CicloEstudosSessao sessao;
	
	@Autowired
	private DisciplinaService disciplinaService;
	
	@GetMapping("/cadastro/{uuid}")
	public ModelAndView form(@PathVariable String uuid, PeriodoCicloEstudos periodoCicloEstudos, ModelMap model, RedirectAttributes redirect) {
		CicloEstudos cicloEstudos = sessao.getCicloEstudos(uuid);
		if (cicloEstudos == null) {
        	addMensagemErro(redirect, getMessage("ciclo-estudos.mensagem.naoEncontrado"));
            return new ModelAndView("redirect:/ciclo-estudos");
        }
		
		DisciplinaPeriodo disciplinaPeriodo = new DisciplinaPeriodo();
		disciplinaPeriodo.setDisciplina(new Disciplina());
		disciplinaPeriodo.getDisciplina().setNome("Auditoria");
		disciplinaPeriodo.setDuracao(120);
		periodoCicloEstudos.getDisciplinasPeriodoPrimeiroDia().add(disciplinaPeriodo);
		
		disciplinaPeriodo = new DisciplinaPeriodo();
		disciplinaPeriodo.setDisciplina(new Disciplina());
		disciplinaPeriodo.getDisciplina().setNome("Contabilidade");
		disciplinaPeriodo.setDuracao(1);
		periodoCicloEstudos.getDisciplinasPeriodoPrimeiroDia().add(disciplinaPeriodo);
		
		return new ModelAndView("periodo-ciclo-estudos/Form");
	}
	
	@PostMapping("/cadastro/{uuid}")
	public ModelAndView cadastro(@PathVariable String uuid, PeriodoCicloEstudos periodoCicloEstudos, ModelMap modelMap, RedirectAttributes redirect, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {		
		CicloEstudos cicloEstudos = sessao.getCicloEstudos(uuid);
		/*
		if(bindingResult.hasErrors()) {
			addMensagensErroValidacao(modelMap, bindingResult);
			return form(cicloEstudos.getUuid(), periodoCicloEstudos, modelMap, redirect);
		}
		*/
		
		if (cicloEstudos == null) {
        	addMensagemErro(redirect, getMessage("ciclo-estudos.mensagem.naoEncontrado"));
            return new ModelAndView("redirect:/ciclo-estudos");
        }
		
		periodoCicloEstudos = new PeriodoCicloEstudos();
		modelMap.addAttribute(periodoCicloEstudos);
		addMensagemSucesso(modelMap, getMessage("periodo-ciclo-estudos.mensagem.sucesso"));
		
		return form(cicloEstudos.getUuid(), periodoCicloEstudos, modelMap, redirect);
	}
	
	
	@ModelAttribute("disciplinasUsuario")
	public List<Disciplina> getDisciplinasUsuario() {
		return disciplinaService.disciplinasUsuario(getUsuarioLogado());
	}
}
