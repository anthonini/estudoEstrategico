package br.com.anthonini.estudoEstrategico.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.com.anthonini.estudoEstrategico.mail.CadastroUsuarioMailer;
import br.com.anthonini.estudoEstrategico.model.Usuario;
import br.com.anthonini.estudoEstrategico.repository.UsuarioRepository;
import br.com.anthonini.estudoEstrategico.service.exception.EmailUsuarioJaCadastradoException;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private CadastroUsuarioMailer mailer;
	
	@Transactional
	public void cadastrar(Usuario usuario) {
		Optional<Usuario> usuarioExistentePorEmail = repository.findByEmail(usuario.getEmail());
		
		if(usuarioExistentePorEmail.isPresent() && !usuarioExistentePorEmail.get().equals(usuario)) {
			throw new EmailUsuarioJaCadastradoException();
		}
		
		if( usuario.isNovo() || !StringUtils.isEmpty(usuario.getSenha())) {
			usuario.setSenha(this.passwordEncoder.encode(usuario.getSenha()));
			usuario.setConfirmacaoSenha(usuario.getSenha());
		}
		
		usuario.setAtivo(true);
		
		repository.save(usuario);
		
		mailer.enviarEmailConfirmacao(usuario);
	}
}
