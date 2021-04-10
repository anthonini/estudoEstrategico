package br.com.anthonini.estudoEstrategico.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.anthonini.arquitetura.controller.AbstractController;
import br.com.anthonini.estudoEstrategico.model.CicloEstudos;
import br.com.anthonini.estudoEstrategico.security.UsuarioSistema;
import br.com.anthonini.estudoEstrategico.service.CicloEstudosService;
import br.com.anthonini.estudoEstrategico.service.exception.NomeEntidadeJaCadastradaException;
import br.com.anthonini.estudoEstrategico.service.exception.UsuarioSemPermissaoParaRealizarEssaOperacao;

@Controller
@RequestMapping("/ciclo-estudos")
public class CicloEstudosController extends AbstractController {
	
	@Autowired
	private CicloEstudosService service;

	@GetMapping("/cadastro")
	public ModelAndView cadastro(CicloEstudos cicloEstudos, ModelMap model) {
		return new ModelAndView("ciclo-estudos/Form");
	}
	
	@PostMapping({"/cadastro", "/{\\d+}"})
	public ModelAndView cadastro(@Valid CicloEstudos cicloEstudos, BindingResult bindingResult, @AuthenticationPrincipal UsuarioSistema usuarioSistema, ModelMap modelMap, RedirectAttributes redirect) {
		if(bindingResult.hasErrors()) {
			addMensagensErroValidacao(modelMap, bindingResult);
			return cadastro(cicloEstudos, modelMap);
		}

		String view = cicloEstudos.isNovo() ? "/ciclo-estudos/cadastro" : "/ciclo-estudos";
		
		try {
			service.salvar(cicloEstudos, usuarioSistema.getUsuario());
			addMensagemSucesso(redirect, "Ciclo de Estudos salvo com sucesso!");
		} catch (NomeEntidadeJaCadastradaException e) {
			bindingResult.rejectValue("nome", e.getMessage(), e.getMessage());
			addMensagensErroValidacao(modelMap, bindingResult);
			return cadastro(cicloEstudos, modelMap);
		} catch (UsuarioSemPermissaoParaRealizarEssaOperacao e) {
			addMensagemErro(redirect, e.getMessage());
		}
		
		return new ModelAndView("redirect:"+view);
	}
}
