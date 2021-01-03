package br.com.anthonini.estudoEstrategico.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	
	
	@GetMapping("/cadastro")
	public ModelAndView cadastro(Usuario usuario, ModelMap model) {
		return new ModelAndView("usuario/Form");
	}
	
	@PostMapping("/cadastro")
	public ModelAndView cadastro(@Valid Usuario usuario, BindingResult bindingResult, ModelMap modelMap, RedirectAttributes redirect) {
		if(bindingResult.hasErrors()) {
			addMensagensErroValidacao(modelMap, bindingResult);
			return cadastro(usuario, modelMap);
		}
		
		try {
			service.cadastrar(usuario);
		} catch (EmailUsuarioJaCadastradoException e) {
			bindingResult.rejectValue("email", e.getMessage(), e.getMessage());
			addMensagensErroValidacao(modelMap, bindingResult);
			return cadastro(usuario, modelMap);
		} catch (MailSendException e) {
			addMensagemErro(modelMap, getMessage("email.mensagem.falhaEnvioEmail"));
			return cadastro(usuario, modelMap);
		} catch (MailAuthenticationException e) {
			addMensagemErro(modelMap, getMessage("email.mensagem.falhaAntenticarEmail"));
			return cadastro(usuario, modelMap);
		}
		
		addMensagemInfo(redirect, getMessage("cadastro.usuario.mensagem.ativarConta", usuario.getEmail()));
		addMensagemSucesso(redirect, getMessage("cadastro.usuario.mensagem.sucesso"));
		
		return new ModelAndView("redirect:/login");
	}
	
	@GetMapping("/confirmacao")
	public String confirmacaoCadastro(@RequestParam("token") String token, ModelMap model, RedirectAttributes redirectAttributes) {		
		TokenConfirmacaoUsuario tokenConfirmacao = tokenConfirmacaoUsuarioService.getTokenConfirmacao(token);
		if(tokenConfirmacaoUsuarioService.tokenValido(tokenConfirmacao)) {
			service.ativar(tokenConfirmacao.getUsuario());
			addMensagemSucesso(redirectAttributes, getMessage("confirmacao.usuario.mensagem.usuarioConfirmado"));
		}else if(tokenConfirmacao != null && tokenConfirmacao.getUsuario().isAtivo()) {
			addMensagemInfo(redirectAttributes, getMessage("confirmacao.usuario.mensagem.usuarioJaConfirmado"));
		}else {
			addMensagemErro(model, getMessage("confirmacao.usuario.mensagem.tokenInvalido"));
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
	public String reenviarEmailConfirmacao(String email, ModelMap model, RedirectAttributes redirectAttributes) {
		if(email == null || email.isEmpty()) {
			addMensagemErro(model, "E-mail é obrigatório");
			return reenviarEmailConfirmacao(null, model);
		}
		
		try {
			service.reenviarEmailConfirmacao(email);
			addMensagemSucesso(redirectAttributes, getMessage("confirmacao.usuario.mensagem.sucesso"));
		} catch (MailSendException e) {
			addMensagemErro(model, getMessage("email.mensagem.falhaEnvioEmail"));
			return reenviarEmailConfirmacao(null, model);
		} catch (MailAuthenticationException e) {
			addMensagemErro(model, getMessage("email.mensagem.falhaAntenticarEmail"));
			return reenviarEmailConfirmacao(null, model);
		} catch (UsuarioNaoEncontradoException e) {
			addMensagemErro(model, getMessage("confirmacao.usuario.mensagem.usuarioNaoEncontrado", email));
			return reenviarEmailConfirmacao(null, model);
		}  catch (UsuarioJaConfirmadoException e) {
			addMensagemInfo(redirectAttributes, getMessage("confirmacao.usuario.mensagem.usuarioJaConfirmado"));
		}
	    
		return "redirect:/login";
	}
	
	@GetMapping("/recuperar-senha")
	public ModelAndView recuperarSenha(String email, ModelMap model) {
		model.addAttribute("email", email);
	    return new ModelAndView("usuario/RecuperarSenha");
	}
	
	@PostMapping("/recuperar-senha")
	public ModelAndView recuperarSenha(String email, ModelMap model, RedirectAttributes redirectAttributes) {	    
		if(email == null || email.isEmpty()) {
			addMensagemErro(model, "E-mail é obrigatório");
			return recuperarSenha(email, model);
		}
		
		try {
			service.recuperarSenha(email);
			addMensagemSucesso(redirectAttributes, getMessage("recuperacao.senha.usuario.mensagem.sucesso"));
			
			return new ModelAndView("redirect:/login");
		} catch (MailSendException e) {
			addMensagemErro(model, getMessage("email.mensagem.falhaEnvioEmail"));
			return recuperarSenha(email, model);
		} catch (MailAuthenticationException e) {
			addMensagemErro(model, getMessage("email.mensagem.falhaAntenticarEmail"));
			return recuperarSenha(email, model);
		}
	}
	
	@GetMapping("/alterar-senha/{token}")
	public ModelAndView iniciarAlterarSenha(@PathVariable String token, PasswordDTO passwordDTO, ModelMap model) {		
		if(tokenResetarSenhaUsuarioService.tokenValido(token)) {
			return new ModelAndView("usuario/AlterarSenha");
		}else {
			addMensagemErro(model, getMessage("resetar.senha.usuario.mensagem.tokenInvalido"));
			return recuperarSenha(null, model);
		}
	}
	
	@PostMapping("/alterar-senha/{token}")
	public ModelAndView alterarSenha(@Valid PasswordDTO passwordDTO, BindingResult bindingResult, @PathVariable String token, ModelMap model, RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors()) {
			addMensagensErroValidacao(model, bindingResult);
			return iniciarAlterarSenha(token, passwordDTO, model);
		}
		
		if(tokenResetarSenhaUsuarioService.tokenValido(token)) {
			try {
				TokenResetarSenhaUsuario tokenResetarSenha = tokenResetarSenhaUsuarioService.getToken(token);
				service.alterarSenha(tokenResetarSenha.getUsuario(), passwordDTO.getSenha());
				addMensagemSucesso(redirectAttributes, getMessage("resetar.senha.usuario.mensagem.sucesso"));
				return new ModelAndView("redirect:/login");
			} catch (MailSendException e) {
				addMensagemErro(model, getMessage("email.mensagem.falhaEnvioEmail"));
				return iniciarAlterarSenha(token, passwordDTO, model);
			} catch (MailAuthenticationException e) {
				addMensagemErro(model, getMessage("email.mensagem.falhaAntenticarEmail"));
				return iniciarAlterarSenha(token, passwordDTO, model);
			} 
		} else {
			addMensagemErro(model, getMessage("resetar.senha.usuario.mensagem.tokenInvalido"));
			return recuperarSenha(null, model);
		}
	}
}
