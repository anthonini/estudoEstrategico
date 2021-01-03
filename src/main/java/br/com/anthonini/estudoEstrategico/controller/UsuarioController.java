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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.anthonini.arquitetura.controller.AbstractController;
import br.com.anthonini.estudoEstrategico.dto.PasswordDTO;
import br.com.anthonini.estudoEstrategico.model.TokenConfirmacaoUsuario;
import br.com.anthonini.estudoEstrategico.model.TokenResetarSenhaUsuario;
import br.com.anthonini.estudoEstrategico.model.Usuario;
import br.com.anthonini.estudoEstrategico.service.TokenConfirmacaoUsuarioService;
import br.com.anthonini.estudoEstrategico.service.TokenResetarSenhaUsuarioService;
import br.com.anthonini.estudoEstrategico.service.UsuarioService;
import br.com.anthonini.estudoEstrategico.service.exception.EmailUsuarioJaCadastradoException;
import br.com.anthonini.estudoEstrategico.service.exception.UsuarioJaConfirmadoException;
import br.com.anthonini.estudoEstrategico.service.exception.UsuarioNaoEncontradoException;

@Controller
@RequestMapping("/usuario")
public class UsuarioController extends AbstractController {

	@Autowired
	private UsuarioService service;
	
	@Autowired
	private TokenConfirmacaoUsuarioService tokenConfirmacaoUsuarioService;
	
	@Autowired
	private TokenResetarSenhaUsuarioService tokenResetarSenhaUsuarioService;
	
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
			addMensagemErro(modelMap, messageSource.getMessage("email.mensagem.falhaEnvioEmail", null, locale));
			return cadastro(usuario, modelMap);
		} catch (MailAuthenticationException e) {
			addMensagemErro(modelMap, messageSource.getMessage("email.mensagem.falhaAntenticarEmail", null, locale));
			return cadastro(usuario, modelMap);
		}
		
		addMensagemInfo(redirect, messageSource.getMessage("cadastro.usuario.mensagem.ativarConta", new Object[] {usuario.getEmail()}, locale));
		addMensagemSucesso(redirect, messageSource.getMessage("cadastro.usuario.mensagem.sucesso", null, locale));
		
		return new ModelAndView("redirect:/login");
	}
	
	@GetMapping("/confirmacao")
	public String confirmacaoCadastro(WebRequest request, @RequestParam("token") String token, ModelMap model, RedirectAttributes redirectAttributes) {
		Locale locale = request.getLocale();
		
		TokenConfirmacaoUsuario tokenConfirmacao = tokenConfirmacaoUsuarioService.getTokenConfirmacao(token);
		if(tokenConfirmacaoUsuarioService.tokenValido(tokenConfirmacao)) {
			service.ativar(tokenConfirmacao.getUsuario());
			addMensagemSucesso(redirectAttributes, messageSource.getMessage("confirmacao.usuario.mensagem.usuarioConfirmado", null, locale));
		}else if(tokenConfirmacao != null && tokenConfirmacao.getUsuario().getAtivo()) {
			addMensagemInfo(redirectAttributes, messageSource.getMessage("confirmacao.usuario.mensagem.usuarioJaConfirmado", null, locale));
		}else {
			addMensagemErro(model, messageSource.getMessage("confirmacao.usuario.mensagem.tokenInvalido", null, locale));
			return reenviarEmailConfirmacao(tokenConfirmacao, model);
		}
	    
	    return "redirect:/login";
	}
	
	@GetMapping("/reenviar-email-confirmacao")
	public String reenviarEmailConfirmacao(TokenConfirmacaoUsuario tokenConfirmacao, ModelMap model) {
		if(tokenConfirmacao != null && tokenConfirmacao.getUsuario() != null) {
			model.addAttribute("email", tokenConfirmacao.getUsuario().getEmail());
		}
	    
	    return "usuario/ReenviarEmailConfirmacao";
	}
	
	@PostMapping("/reenviar-email-confirmacao")
	public String reenviarEmailConfirmacao(String email, ModelMap model, WebRequest request, RedirectAttributes redirectAttributes) {
		if(email == null || email.isEmpty()) {
			addMensagemErro(model, "E-mail é obrigatório");
			return reenviarEmailConfirmacao(null, model);
		}
		
		Locale locale = request.getLocale();
		
		try {
			service.reenviarEmailConfirmacao(email);
			addMensagemSucesso(redirectAttributes, messageSource.getMessage("confirmacao.usuario.mensagem.sucesso", null, locale));
		} catch (MailSendException e) {
			addMensagemErro(model, messageSource.getMessage("email.mensagem.falhaEnvioEmail", null, locale));
			return reenviarEmailConfirmacao(null, model);
		} catch (MailAuthenticationException e) {
			addMensagemErro(model, messageSource.getMessage("email.mensagem.falhaAntenticarEmail", null, locale));
			return reenviarEmailConfirmacao(null, model);
		} catch (UsuarioNaoEncontradoException e) {
			addMensagemErro(model, messageSource.getMessage("confirmacao.usuario.mensagem.usuarioNaoEncontrado", new Object[] {email}, locale));
			return reenviarEmailConfirmacao(null, model);
		}  catch (UsuarioJaConfirmadoException e) {
			addMensagemInfo(redirectAttributes, messageSource.getMessage("confirmacao.usuario.mensagem.usuarioJaConfirmado", null, locale));
		}
	    
		return "redirect:/login";
	}
	
	@GetMapping("/recuperar-senha")
	public ModelAndView recuperarSenha(String email, ModelMap model) {
		model.addAttribute("email", email);
	    return new ModelAndView("usuario/RecuperarSenha");
	}
	
	@PostMapping("/recuperar-senha")
	public ModelAndView recuperarSenha(String email, ModelMap model, WebRequest request, RedirectAttributes redirectAttributes) {	    
		if(email == null || email.isEmpty()) {
			addMensagemErro(model, "E-mail é obrigatório");
			return recuperarSenha(email, model);
		}
		
		try {
			service.recuperarSenha(email);
			addMensagemSucesso(redirectAttributes, messageSource.getMessage("recuperacao.senha.usuario.mensagem.sucesso", null, request.getLocale()));
			
			return new ModelAndView("redirect:/login");
		} catch (MailSendException e) {
			addMensagemErro(model, messageSource.getMessage("email.mensagem.falhaEnvioEmail", null, request.getLocale()));
			return recuperarSenha(email, model);
		} catch (MailAuthenticationException e) {
			addMensagemErro(model, messageSource.getMessage("email.mensagem.falhaAntenticarEmail", null, request.getLocale()));
			return recuperarSenha(email, model);
		}
	}
	
	@GetMapping("/alterar-senha/{token}")
	public ModelAndView iniciarAlterarSenha(WebRequest request, @PathVariable String token, PasswordDTO passwordDTO, ModelMap model) {
		Locale locale = request.getLocale();
		
		if(tokenResetarSenhaUsuarioService.tokenValido(token)) {
			return new ModelAndView("usuario/AlterarSenha");
		}else {
			addMensagemErro(model, messageSource.getMessage("resetar.senha.usuario.mensagem.tokenInvalido", null, locale));
			return recuperarSenha(null, model);
		}
	}
	
	@PostMapping("/alterar-senha/{token}")
	public ModelAndView alterarSenha(@Valid PasswordDTO passwordDTO, BindingResult bindingResult, @PathVariable String token, ModelMap model, WebRequest request, RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors()) {
			addMensagensErroValidacao(model, bindingResult);
			return iniciarAlterarSenha(request, token, passwordDTO, model);
		}
		
		Locale locale = request.getLocale();
		
		if(tokenResetarSenhaUsuarioService.tokenValido(token)) {
			try {
				TokenResetarSenhaUsuario tokenResetarSenha = tokenResetarSenhaUsuarioService.getToken(token);
				service.alterarSenha(tokenResetarSenha.getUsuario(), passwordDTO.getSenha());
				addMensagemSucesso(redirectAttributes, messageSource.getMessage("resetar.senha.usuario.mensagem.sucesso", null, request.getLocale()));
				return new ModelAndView("redirect:/login");
			} catch (MailSendException e) {
				addMensagemErro(model, messageSource.getMessage("email.mensagem.falhaEnvioEmail", null, locale));
				return iniciarAlterarSenha(request, token, passwordDTO, model);
			} catch (MailAuthenticationException e) {
				addMensagemErro(model, messageSource.getMessage("email.mensagem.falhaAntenticarEmail", null, locale));
				return iniciarAlterarSenha(request, token, passwordDTO, model);
			} 
		} else {
			addMensagemErro(model, messageSource.getMessage("resetar.senha.usuario.mensagem.tokenInvalido", null, locale));
			return recuperarSenha(null, model);
		}
	}
}
