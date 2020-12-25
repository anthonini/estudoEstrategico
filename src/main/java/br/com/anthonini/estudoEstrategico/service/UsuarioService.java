package br.com.anthonini.estudoEstrategico.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.com.anthonini.estudoEstrategico.model.Usuario;
import br.com.anthonini.estudoEstrategico.repository.UsuarioRepository;
import br.com.anthonini.estudoEstrategico.service.event.usuario.CadastroUsuarioEvent;
import br.com.anthonini.estudoEstrategico.service.event.usuario.ReenvioEmailConfirmacaoEvent;
import br.com.anthonini.estudoEstrategico.service.exception.EmailUsuarioJaCadastradoException;
import br.com.anthonini.estudoEstrategico.service.exception.UsuarioJaConfirmadoException;
import br.com.anthonini.estudoEstrategico.service.exception.UsuarioNaoEncontradoException;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	ApplicationEventPublisher publisher;
	
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
		
		repository.save(usuario);
		publisher.publishEvent(new CadastroUsuarioEvent(usuario));
	}
	
	@Transactional
	public void ativar(Usuario usuario) {
		if(usuario.getAtivo() != null && !usuario.getAtivo()) {
			usuario.setAtivo(true);
			repository.save(usuario);
		}
	}

	@Transactional
	public void reenviarEmailConfirmacao(String email) {
		Optional<Usuario> usuarioExistentePorEmail = repository.findByEmail(email);
		
		if(usuarioExistentePorEmail.isPresent()) {
			Usuario usuario = usuarioExistentePorEmail.get();
			if(usuario.getAtivo()) {
				throw new UsuarioJaConfirmadoException();
			} else {
				publisher.publishEvent(new ReenvioEmailConfirmacaoEvent(usuario));
			}
		} else {
			throw new UsuarioNaoEncontradoException();
		}
	}
}
