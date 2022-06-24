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
import br.com.anthonini.estudoEstrategico.model.GrupoUsuario;
import br.com.anthonini.estudoEstrategico.repository.helper.grupousuario.filter.GrupoUsuarioFilter;
import br.com.anthonini.estudoEstrategico.security.UsuarioSistema;
import br.com.anthonini.estudoEstrategico.service.GrupoUsuarioService;
import br.com.anthonini.estudoEstrategico.service.PermissaoService;
import br.com.anthonini.estudoEstrategico.service.exception.NaoEPossivelRemoverEntidadeException;
import br.com.anthonini.estudoEstrategico.service.exception.NomeEntidadeJaCadastradaException;

@Controller
@RequestMapping("/grupo-usuario")
public class GrupoUsuarioController extends AbstractController {
	
	@Autowired
	private PermissaoService permissaoService;
	
	@Autowired
	private GrupoUsuarioService service;

	@GetMapping("/cadastro")
	public ModelAndView cadastro(GrupoUsuario grupoUsuario, ModelMap model) {
		model.addAttribute("permissoes", permissaoService.findAll());
		return new ModelAndView("grupo-usuario/Form");
	}
	
	@PostMapping({"/cadastro", "/{\\d+}"})
	public ModelAndView cadastro(@Valid GrupoUsuario grupoUsuario, BindingResult bindingResult, @AuthenticationPrincipal UsuarioSistema usuarioSistema, ModelMap modelMap, RedirectAttributes redirect) {
		if(bindingResult.hasErrors()) {
			addMensagensErroValidacao(modelMap, bindingResult);
			return cadastro(grupoUsuario, modelMap);
		}

		String view = grupoUsuario.isNovo() ? "/grupo-usuario/cadastro" : "/grupo-usuario";
		
		try {
			service.cadastrar(grupoUsuario);
			addMensagemSucesso(redirect, getMessage("grupo-usuario.mensagem.sucesso"));
		} catch (NomeEntidadeJaCadastradaException e) {
			bindingResult.rejectValue("nome", e.getMessage(), e.getMessage());
			addMensagensErroValidacao(modelMap, bindingResult);
			return cadastro(grupoUsuario, modelMap);
		}
		
		return new ModelAndView("redirect:"+view);
	}
	
	@GetMapping
	public ModelAndView listar(GrupoUsuarioFilter filter, HttpServletRequest httpServletRequest, @PageableDefault(size = 10) @SortDefault(value="nome") Pageable pageable) {
		ModelAndView mv = new ModelAndView("grupo-usuario/List");
		PageWrapper<GrupoUsuario> paginaWrapper = new PageWrapper<>(service.filtrar(filter,pageable),httpServletRequest);
        mv.addObject("pagina", paginaWrapper);
		
		return mv;
	}
	
	@GetMapping("/{id}")
	public ModelAndView alterar(@PathVariable("id") GrupoUsuario grupoUsuario, ModelMap model, RedirectAttributes redirect) {
		if (grupoUsuario == null) {
            addMensagemErro(redirect, getMessage("grupo-usuario.mensagem.naoEncontrado"));
            return new ModelAndView("redirect:/grupo-usuario");
        }

		model.addAttribute("grupoUsuario", grupoUsuario);
        return cadastro(grupoUsuario, model);
    }
	
	@DeleteMapping("/{id}")
	public @ResponseBody ResponseEntity<?> delete(@PathVariable("id") GrupoUsuario grupoUsuario) {
		if(grupoUsuario != null) {
			try {
				service.remover(grupoUsuario);
			} catch (NaoEPossivelRemoverEntidadeException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.badRequest().body("Grupo de Usuário não encontrado!");
		}
	}
}
