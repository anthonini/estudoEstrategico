package br.com.anthonini.estudoEstrategico.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.anthonini.arquitetura.controller.AbstractController;
import br.com.anthonini.estudoEstrategico.model.Usuario;
import br.com.anthonini.estudoEstrategico.service.UsuarioService;
import br.com.anthonini.estudoEstrategico.service.exception.EmailUsuarioJaCadastradoException;

@Controller
@RequestMapping("/usuario")
public class UsuarioController extends AbstractController {

	@Autowired
	private UsuarioService service;
	
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
			addMensagemErro(modelMap, "Falha ao enviar email. Verifique as configurações de email e/ou se o seu antivírus bloqueou o envio.");
			return cadastro(usuario, modelMap);
		} catch (MailAuthenticationException e) {
			addMensagemErro(modelMap, "Falha ao autenticar com o servidor de email. Verifique as credenciais e/ou se o seu servidor de email está bloqueando o acesso.");
			return cadastro(usuario, modelMap);
		}
		
		addMensagemInfo(redirect, "Ative já sua conta através do e-mail "+usuario.getEmail());
		addMensagemSucesso(redirect, "Seu cadastro foi efetuado com sucesso!");
		
		return new ModelAndView("redirect:/login");
	}
}
