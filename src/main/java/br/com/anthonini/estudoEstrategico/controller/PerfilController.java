package br.com.anthonini.estudoEstrategico.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import br.com.anthonini.estudoEstrategico.dto.PerfilDTO;
import br.com.anthonini.estudoEstrategico.security.UsuarioSistema;
import br.com.anthonini.estudoEstrategico.service.UsuarioService;
import br.com.anthonini.estudoEstrategico.service.exception.EmailUsuarioJaCadastradoException;
import br.com.anthonini.estudoEstrategico.service.exception.SenhaNaoConfirmadaException;

@Controller
@RequestMapping("/perfil")
public class PerfilController extends AbstractController {

	@Autowired
	private UsuarioService service;
	
	@GetMapping
	public ModelAndView cadastro(@AuthenticationPrincipal UsuarioSistema usuarioSistema, ModelMap model) {
		return cadastro(new PerfilDTO(usuarioSistema.getUsuario()), model);
	}
	
	@PostMapping
	public ModelAndView cadastro(@Valid PerfilDTO perfilDTO, BindingResult bindingResult, @AuthenticationPrincipal UsuarioSistema usuarioSistema, ModelMap modelMap, RedirectAttributes redirect) {
		if(bindingResult.hasErrors()) {
			addMensagensErroValidacao(modelMap, bindingResult);
			return cadastro(perfilDTO, modelMap);
		}
		
		try {
			service.atualizarDados(usuarioSistema.getUsuario(), perfilDTO);
		} catch (SenhaNaoConfirmadaException e) {
			bindingResult.rejectValue("senha", e.getMessage(), e.getMessage());
			addMensagensErroValidacao(modelMap, bindingResult);
			return cadastro(perfilDTO, modelMap);
		} catch (EmailUsuarioJaCadastradoException e) {
			bindingResult.rejectValue("email", e.getMessage(), e.getMessage());
			addMensagensErroValidacao(modelMap, bindingResult);
			return cadastro(perfilDTO, modelMap);
		}
		
		addMensagemSucesso(redirect, getMessage("perfil.atualizacao.mensagem.sucesso"));
		
		return new ModelAndView("redirect:/Perfil");
	}
	
	@PutMapping("/alterar-tema")
	public @ResponseBody ResponseEntity<?> alterarTema(boolean modoEscuro, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		try {
			service.alterarTema(usuarioSistema.getUsuario(), modoEscuro);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Não foi possível alterar o tema!");
		}
		return ResponseEntity.ok(usuarioSistema.getUsuario().getTema().getNavbarClass());
	}
	
	private ModelAndView cadastro(PerfilDTO perfilDTO, ModelMap model) {
		model.addAttribute("perfilDTO", perfilDTO);
		return new ModelAndView("usuario/Perfil");
	}
}
