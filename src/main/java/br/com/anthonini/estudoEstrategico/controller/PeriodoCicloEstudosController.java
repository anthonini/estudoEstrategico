package br.com.anthonini.estudoEstrategico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.anthonini.arquitetura.controller.AbstractController;
import br.com.anthonini.estudoEstrategico.model.CicloEstudos;
import br.com.anthonini.estudoEstrategico.model.DiaPeriodoCicloEstudos;
import br.com.anthonini.estudoEstrategico.model.PeriodoCicloEstudos;
import br.com.anthonini.estudoEstrategico.service.PeriodoCicloEstudosService;
import br.com.anthonini.estudoEstrategico.service.exception.ErrosValidacaoException;
import br.com.anthonini.estudoEstrategico.sessao.CicloEstudosSessao;
import br.com.anthonini.estudoEstrategico.sessao.PeriodoCicloEstudosSessao;

@Controller
@RequestMapping("/periodo-ciclo-estudos")
public class PeriodoCicloEstudosController extends AbstractController {
	
	@Autowired
	private CicloEstudosSessao cicloEstudosSessao;
	
	@Autowired
	private PeriodoCicloEstudosSessao sessao;
	
	@Autowired
	private PeriodoCicloEstudosService service;
	
	@GetMapping("/novo")
	public ModelAndView iniciarForm(String cicloId, RedirectAttributes redirect) {
		CicloEstudos cicloEstudos = cicloEstudosSessao.getCicloEstudos(cicloId);
		if (cicloEstudos == null) {
        	addMensagemErro(redirect, getMessage("ciclo-estudos.mensagem.naoEncontrado"));
            return new ModelAndView("redirect:/ciclo-estudos");
        }
		
		PeriodoCicloEstudos periodoCicloEstudos = new PeriodoCicloEstudos();
		periodoCicloEstudos.setCicloEstudos(cicloEstudos);
		sessao.adicionar(periodoCicloEstudos);
		
		return new ModelAndView("redirect:/periodo-ciclo-estudos/cadastro?cicloId="+cicloEstudos.getUuid()+"&uuid="+periodoCicloEstudos.getUuid());
	}
	
	@GetMapping("/cadastro")
	public ModelAndView form(String cicloId, String uuid, ModelMap model, RedirectAttributes redirect) {
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
		
		model.addAttribute("primeiroDia", DiaPeriodoCicloEstudos.PRIMEIRO);
		model.addAttribute("segundoDia", DiaPeriodoCicloEstudos.SEGUNDO);
		model.addAttribute(cicloEstudos);
		model.addAttribute(periodoCicloEstudos);
		
		return new ModelAndView("periodo-ciclo-estudos/Form");
	}
	
	@PostMapping("/cadastro")
	public ModelAndView cadastro(String cicloId, String uuid, PeriodoCicloEstudos periodoCicloEstudos, BindingResult bindingResult, RedirectAttributes redirect) {		
		CicloEstudos cicloEstudos = cicloEstudosSessao.getCicloEstudos(cicloId);
		if (cicloEstudos == null) {
        	addMensagemErro(redirect, getMessage("ciclo-estudos.mensagem.naoEncontrado"));
            return new ModelAndView("redirect:/ciclo-estudos");
        }
		
		periodoCicloEstudos = sessao.getPeriodoCicloEstudos(uuid);
		if (periodoCicloEstudos == null) {
        	addMensagemErro(redirect, getMessage("periodo-ciclo-estudos.mensagem.naoEncontrado"));
            return new ModelAndView("/ciclo-estudos/cadastro?id="+cicloId);
        }
		
		try {
			service.adicionar(cicloEstudos, periodoCicloEstudos, bindingResult);
			sessao.remover(uuid);
			addMensagemSucesso(redirect, getMessage("periodo-ciclo-estudos.mensagem.sucesso"));
			
			return iniciarForm(cicloId, redirect);
		} catch (ErrosValidacaoException e) {
			addMensagensErroValidacao(redirect, bindingResult);
			return new ModelAndView("redirect:/periodo-ciclo-estudos/cadastro?cicloId="+cicloId+"&uuid="+periodoCicloEstudos.getUuid());
		}
	}
	
	@GetMapping("/alterar")
	public ModelAndView alterar(String cicloId, Integer index, ModelMap model, RedirectAttributes redirect) {
		CicloEstudos cicloEstudos = cicloEstudosSessao.getCicloEstudos(cicloId);
		if (cicloEstudos == null) {
        	addMensagemErro(redirect, getMessage("ciclo-estudos.mensagem.naoEncontrado"));
            return new ModelAndView("redirect:/ciclo-estudos");
        }
		
		PeriodoCicloEstudos periodoCicloEstudos = null;
		if(index >= 0 && index < cicloEstudos.getPeriodosCicloEstudos().size())
			periodoCicloEstudos = cicloEstudos.getPeriodosCicloEstudos().get(index);
		
		if (periodoCicloEstudos == null) {
        	addMensagemErro(redirect, getMessage("periodo-ciclo-estudos.mensagem.naoEncontrado"));
            return new ModelAndView("/ciclo-estudos/cadastro?id="+cicloId);
        }
		
		model.addAttribute("alteracaoPeriodo", true);
		sessao.adicionar(periodoCicloEstudos);
		
		return form(cicloId, periodoCicloEstudos.getUuid(), model, redirect);
	}
	
	@PutMapping("/atualizar-duracao")
	public @ResponseBody ResponseEntity<?> remover(String uuid, Integer duracao, ModelMap model) {
		PeriodoCicloEstudos periodoCicloEstudos = sessao.getPeriodoCicloEstudos(uuid);
		if(periodoCicloEstudos != null && duracao != null)
			periodoCicloEstudos.setDuracao(duracao);
		
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/disciplinas-periodo")
	public String disciplinasPeriodo(String uuid, DiaPeriodoCicloEstudos dia, ModelMap model) {
		model.addAttribute("disciplinasPeriodo", dia.getDisciplinas(sessao.getPeriodoCicloEstudos(uuid)));
		model.addAttribute("dia",dia);
		return "/periodo-ciclo-estudos/fragments/disciplinas";
	}
}
