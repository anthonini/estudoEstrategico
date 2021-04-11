package br.com.anthonini.estudoEstrategico.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.anthonini.arquitetura.controller.AbstractController;
import br.com.anthonini.arquitetura.controller.page.PageWrapper;
import br.com.anthonini.estudoEstrategico.model.CicloEstudos;
import br.com.anthonini.estudoEstrategico.repository.helper.cicloEstudos.filter.CicloEstudosFilter;
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
	
	@GetMapping
	public ModelAndView listar(CicloEstudosFilter filter, @AuthenticationPrincipal UsuarioSistema usuarioSistema, HttpServletRequest httpServletRequest, @PageableDefault(size = 3) @SortDefault(value="nome") Pageable pageable) {
		ModelAndView mv = new ModelAndView("ciclo-estudos/List");
		filter.setUsuario(usuarioSistema.getUsuario());
		PageWrapper<CicloEstudos> paginaWrapper = new PageWrapper<>(service.filtrar(filter,pageable),httpServletRequest);
        mv.addObject("pagina", paginaWrapper);
		
		return mv;
	}
	
	@GetMapping("/{id}")
	public ModelAndView alterar(@PathVariable("id") CicloEstudos cicloEstudos, @AuthenticationPrincipal UsuarioSistema usuarioSistema, ModelMap model, RedirectAttributes redirect) {
		if (cicloEstudos == null || !usuarioSistema.getUsuario().equals(cicloEstudos.getUsuario())) {
            addMensagemErro(redirect, "Ciclo de Estudos não encontrado!");
            return new ModelAndView("redirect:/ciclo-estudos");
        }

		model.addAttribute("cicloEstudos", cicloEstudos);
        return cadastro(cicloEstudos, model);
    }
}
