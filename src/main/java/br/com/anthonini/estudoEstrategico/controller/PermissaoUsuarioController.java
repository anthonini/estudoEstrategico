package br.com.anthonini.estudoEstrategico.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.anthonini.arquitetura.controller.AbstractController;
import br.com.anthonini.arquitetura.controller.page.PageWrapper;
import br.com.anthonini.estudoEstrategico.model.Usuario;
import br.com.anthonini.estudoEstrategico.repository.helper.permissaousuario.filter.PermissaoUsuarioFilter;
import br.com.anthonini.estudoEstrategico.service.PermissaoService;
import br.com.anthonini.estudoEstrategico.service.PermissaoUsuarioService;

@Controller
@RequestMapping("/permissao-usuario")
public class PermissaoUsuarioController extends AbstractController {
	
	@Autowired
	private PermissaoUsuarioService service;
	
	@Autowired
	private PermissaoService permissaoService;

	@GetMapping
	public ModelAndView listar(PermissaoUsuarioFilter filter, HttpServletRequest httpServletRequest, @PageableDefault(size = 10) @SortDefault(value="pessoa.nome") Pageable pageable) {
		ModelAndView mv = new ModelAndView("permissao-usuario/List");
		PageWrapper<Usuario> paginaWrapper = new PageWrapper<>(service.filtrar(filter,pageable),httpServletRequest);
        mv.addObject("pagina", paginaWrapper);
        mv.addObject("permissoes", permissaoService.findAll());
        mv.addObject("grupos", service.buscarTodosGruposUsuario());
		
		return mv;
	}
	
	@GetMapping("/{idUsuario}")
	public ModelAndView alterar(@PathVariable("idUsuario") Usuario usuario, RedirectAttributes redirect) {
		if (usuario == null) {
            addMensagemErro(redirect, getMessage("permissao-usuario.mensagem.naoEncontrado"));
            return new ModelAndView("redirect:/permissao-usuario");
        }

		ModelAndView mv = new ModelAndView("permissao-usuario/Form");
		mv.addObject("grupos", service.buscarTodosGruposUsuario());
		mv.addObject("usuario", usuario);
		return mv;
    }
	
	@PostMapping
	public ModelAndView cadastro(Usuario usuario, ModelMap modelMap, RedirectAttributes redirect) {
		service.salvar(usuario);
		addMensagemSucesso(redirect, getMessage("permissao-usuario.mensagem.sucesso"));
		
		return new ModelAndView("redirect:/permissao-usuario");
	}
}
