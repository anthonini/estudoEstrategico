package br.com.anthonini.estudoEstrategico.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.anthonini.arquitetura.controller.AbstractController;
import br.com.anthonini.arquitetura.controller.page.PageWrapper;
import br.com.anthonini.estudoEstrategico.model.Disciplina;
import br.com.anthonini.estudoEstrategico.repository.helper.disciplina.filter.DisciplinaFilter;
import br.com.anthonini.estudoEstrategico.security.UsuarioSistema;
import br.com.anthonini.estudoEstrategico.service.DisciplinaService;
import br.com.anthonini.estudoEstrategico.service.NaoEPossivelRemoverEntidadeException;
import br.com.anthonini.estudoEstrategico.service.exception.NomeEntidadeJaCadastradaException;
import br.com.anthonini.estudoEstrategico.service.exception.UsuarioSemPermissaoParaRealizarEssaOperacao;

@Controller
@RequestMapping("/disciplina")
public class DisciplinaController extends AbstractController {
	
	@Autowired
	private DisciplinaService service;

	@GetMapping("/cadastro")
	public ModelAndView cadastro(Disciplina disciplina, ModelMap model) {
		return new ModelAndView("disciplina/Form");
	}
	
	@PostMapping({"/cadastro", "/{\\d+}"})
	public ModelAndView cadastro(@Valid Disciplina disciplina, BindingResult bindingResult, @AuthenticationPrincipal UsuarioSistema usuarioSistema, ModelMap modelMap, RedirectAttributes redirect) {
		if(bindingResult.hasErrors()) {
			addMensagensErroValidacao(modelMap, bindingResult);
			return cadastro(disciplina, modelMap);
		}

		String view = disciplina.isNova() ? "/disciplina/cadastro" : "/disciplina";
		
		try {
			service.cadastrar(disciplina, usuarioSistema.getUsuario());
			addMensagemSucesso(redirect, "Disciplina salva com sucesso!");
		} catch (NomeEntidadeJaCadastradaException e) {
			bindingResult.rejectValue("nome", e.getMessage(), e.getMessage());
			addMensagensErroValidacao(modelMap, bindingResult);
			return cadastro(disciplina, modelMap);
		} catch (UsuarioSemPermissaoParaRealizarEssaOperacao e) {
			addMensagemErro(redirect, e.getMessage());
		}
		
		return new ModelAndView("redirect:"+view);
	}
	
	@GetMapping
	public ModelAndView listar(DisciplinaFilter filter, @AuthenticationPrincipal UsuarioSistema usuarioSistema, HttpServletRequest httpServletRequest, @PageableDefault(size = 3) @SortDefault(value="nome") Pageable pageable) {
		ModelAndView mv = new ModelAndView("disciplina/List");
		filter.setUsuario(usuarioSistema.getUsuario());
		PageWrapper<Disciplina> paginaWrapper = new PageWrapper<>(service.filtrar(filter,pageable),httpServletRequest);
        mv.addObject("pagina", paginaWrapper);
		
		return mv;
	}
	
	@GetMapping("/{id}")
	public ModelAndView alterar(@PathVariable("id") Disciplina disciplina, @AuthenticationPrincipal UsuarioSistema usuarioSistema, ModelMap model, RedirectAttributes redirect) {
		if (disciplina == null || !usuarioSistema.getUsuario().equals(disciplina.getUsuario())) {
            addMensagemErro(redirect, "Disciplina não encontrada");
            return new ModelAndView("redirect:/disciplina");
        }

		model.addAttribute("disciplina", disciplina);
        return cadastro(disciplina, model);
    }
	
	@DeleteMapping("/{id}")
	public @ResponseBody ResponseEntity<?> delete(@PathVariable("id") Disciplina disciplina, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		if(disciplina != null && usuarioSistema.getUsuario().equals(disciplina.getUsuario())) {
			try {
				service.remover(disciplina);
			} catch (NaoEPossivelRemoverEntidadeException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.badRequest().body("Disciplina não encontrada!");
		}
	}
}
