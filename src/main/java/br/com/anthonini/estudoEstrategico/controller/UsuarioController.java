package br.com.anthonini.estudoEstrategico.controller;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.anthonini.arquitetura.controller.AbstractController;
import br.com.anthonini.estudoEstrategico.model.TokenConfirmacaoUsuario;
import br.com.anthonini.estudoEstrategico.model.Usuario;
import br.com.anthonini.estudoEstrategico.service.TokenConfirmacaoUsuarioService;
import br.com.anthonini.estudoEstrategico.service.UsuarioService;
import br.com.anthonini.estudoEstrategico.service.exception.EmailUsuarioJaCadastradoException;

@Controller
@RequestMapping("/usuario")
public class UsuarioController extends AbstractController {

	@Autowired
	private UsuarioService service;
	
	@Autowired
	private TokenConfirmacaoUsuarioService tokenService;
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping("/cadastro")
	public ModelAndView cadastro(Usuario usuario, ModelMap model) {
		return new ModelAndView("usuario/Form");
	}
	
	@PostMapping("/cadastro")
	public ModelAndView cadastro(@Valid Usuario usuario, BindingResult bindingResult, ModelMap modelMap, RedirectAttributes redirect, WebRequest request) {
		if(bindingResult.hasErrors()) {
			addMensagensErroValidacao(modelMap, bindingResult);
			return cadastro(usuario, modelMap);
		}
		
		Locale locale = request.getLocale();
		
		try {
			service.cadastrar(usuario);
		} catch (EmailUsuarioJaCadastradoException e) {
			bindingResult.rejectValue("email", e.getMessage(), e.getMessage());
			addMensagensErroValidacao(modelMap, bindingResult);
			return cadastro(usuario, modelMap);
		} catch (MailSendException e) {
			addMensagemErro(modelMap, messageSource.getMessage("cadastro.usuario.mensagem.falhaEnvioEmail", null, locale));
			return cadastro(usuario, modelMap);
		} catch (MailAuthenticationException e) {
			addMensagemErro(modelMap, messageSource.getMessage("cadastro.usuario.mensagem.falhaAntenticarEmail", null, locale));
			return cadastro(usuario, modelMap);
		}
		
		addMensagemInfo(redirect, messageSource.getMessage("cadastro.usuario.mensagem.ativarConta", new Object[] {usuario.getEmail()}, locale));
		addMensagemSucesso(redirect, messageSource.getMessage("cadastro.usuario.mensagem.sucesso", null, locale));
		
		return new ModelAndView("redirect:/login");
	}
	
	@GetMapping("/confirmacao")
	public String confirmacaoCadastro(WebRequest request, @RequestParam("token") String token, ModelMap model, RedirectAttributes redirectAttributes) {
		Locale locale = request.getLocale();
		
		TokenConfirmacaoUsuario tokenConfirmacao = tokenService.getTokenConfirmacao(token);
		if(tokenService.tokenValido(tokenConfirmacao)) {
			service.ativar(tokenConfirmacao.getUsuario());
			addMensagemSucesso(redirectAttributes, messageSource.getMessage("autenticacao.mensagem.usuarioConfirmado", null, locale));
		} else {
			addMensagemErro(redirectAttributes, messageSource.getMessage("autenticacao.mensagem.tokenInvalido", null, locale));
		}
	    
	    return "redirect:/login";
	}
}
