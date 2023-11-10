package br.com.anthonini.estudoEstrategico.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import br.com.anthonini.estudoEstrategico.controller.validador.UsuarioValidador;
import br.com.anthonini.estudoEstrategico.dto.PerfilDTO;
import br.com.anthonini.estudoEstrategico.model.Pessoa;
import br.com.anthonini.estudoEstrategico.model.Tema;
import br.com.anthonini.estudoEstrategico.model.Usuario;
import br.com.anthonini.estudoEstrategico.repository.UsuarioRepository;
import br.com.anthonini.estudoEstrategico.repository.helper.usuario.filter.UsuarioFilter;
import br.com.anthonini.estudoEstrategico.service.event.usuario.AlteracaoSenhaUsuarioEvent;
import br.com.anthonini.estudoEstrategico.service.event.usuario.CadastroUsuarioEvent;
import br.com.anthonini.estudoEstrategico.service.event.usuario.ReenvioEmailConfirmacaoEvent;
import br.com.anthonini.estudoEstrategico.service.event.usuario.ResetarSenhaUsuarioEvent;
import br.com.anthonini.estudoEstrategico.service.exception.CPFJaCadastradoException;
import br.com.anthonini.estudoEstrategico.service.exception.EmailUsuarioJaCadastradoException;
import br.com.anthonini.estudoEstrategico.service.exception.ErrosValidacaoException;
import br.com.anthonini.estudoEstrategico.service.exception.OperacaoDeveSerReiniciada;
import br.com.anthonini.estudoEstrategico.service.exception.SenhaNaoConfirmadaException;
import br.com.anthonini.estudoEstrategico.service.exception.UsuarioJaConfirmadoException;
import br.com.anthonini.estudoEstrategico.service.exception.UsuarioNaoEncontradoException;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private UsuarioValidador usuarioValidador;
	
	@Transactional
	public void cadastrar(Usuario usuario) {
		if(usuario.isNovo() && repository.existsByPessoaCpf(Pessoa.removerFormatoCPF(usuario.getPessoa().getCpf()))) {
			throw new CPFJaCadastradoException();
		}
		
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
		if(!usuario.isAtivo()) {
			usuario.setAtivo(true);
			repository.save(usuario);
		}
	}

	@Transactional
	public void reenviarEmailConfirmacao(String email) {
		Optional<Usuario> usuarioExistentePorEmail = repository.findByEmail(email);
		
		if(usuarioExistentePorEmail.isPresent()) {
			Usuario usuario = usuarioExistentePorEmail.get();
			if(usuario.isAtivo()) {
				throw new UsuarioJaConfirmadoException();
			} else {
				publisher.publishEvent(new ReenvioEmailConfirmacaoEvent(usuario));
			}
		} else {
			throw new UsuarioNaoEncontradoException();
		}
	}
	
	@Transactional
	public void recuperarSenha(String email) {
		Optional<Usuario> usuarioExistentePorEmail = repository.findByEmail(email);
		
		if(usuarioExistentePorEmail.isPresent()) {
			Usuario usuario = usuarioExistentePorEmail.get();
			
			if(usuario.isAtivo()) {
				publisher.publishEvent(new ResetarSenhaUsuarioEvent(usuario));
			} else {
				throw new UsuarioNaoEncontradoException();
			}
		}
	}

	@Transactional
	public void alterarSenha(Usuario usuario, String senha) {
		usuario.setSenha(this.passwordEncoder.encode(senha));
		usuario.setConfirmacaoSenha(usuario.getSenha());
		repository.save(usuario);
		publisher.publishEvent(new AlteracaoSenhaUsuarioEvent(usuario));
	}
	
	@Transactional
	public void atualizarDados(Usuario usuario, PerfilDTO perfilDTO) {
		if(!this.passwordEncoder.matches(perfilDTO.getSenha(), usuario.getSenha())) {
			throw new SenhaNaoConfirmadaException();
		}
		Optional<Usuario> usuarioExistentePorEmail = repository.findByEmail(perfilDTO.getEmail());
		
		if(usuarioExistentePorEmail.isPresent() && !usuarioExistentePorEmail.get().equals(usuario)) {
			throw new EmailUsuarioJaCadastradoException();
		}
		
		usuario.getPessoa().setNome(perfilDTO.getNome());
		usuario.setEmail(perfilDTO.getEmail());
		repository.save(usuario);
	}
	
	@Transactional
	public void alterarTema(Usuario usuario, boolean modoEscuro) {
		if(modoEscuro) {
			usuario.setTema(Tema.ESCURO);
		} else {
			usuario.setTema(Tema.CLARO);
		}
			
		repository.save(usuario);
	}
	
	public Page<Usuario> filtrar(UsuarioFilter filter, Pageable pageable) {
		return repository.filtrar(filter, pageable);
	}

	@Transactional
	public void alterar(Usuario usuario, BindingResult bindingResult) {
		if(usuario.isNovo()) {
			throw new OperacaoDeveSerReiniciada();
		}
		
		Optional<Usuario> usuarioBancoOptional = repository.findById(usuario.getId());
		if(!usuarioBancoOptional.isPresent()) {
			throw new OperacaoDeveSerReiniciada();
		}
		
		Usuario usuarioBanco = usuarioBancoOptional.get();
		
		//Se não houver alteração de senha utiliza a que já está cadastrada no banco
		if(StringUtils.isEmpty(usuario.getSenha())) {
			usuario.setSenha(usuarioBanco.getSenha());
			usuario.setConfirmacaoSenha(usuario.getSenha());
		}
		
		usuarioValidador.validate(usuario, bindingResult);
		if(bindingResult.hasErrors()) {
			throw new ErrosValidacaoException();
		}
		
		usuario.getPessoa().setId(usuarioBanco.getPessoa().getId());
		
		//Alteração de senha
		if(!StringUtils.isEmpty(usuario.getSenha()) && !usuario.getSenha().equals(usuarioBanco.getSenha())) {
			usuario.setSenha(this.passwordEncoder.encode(usuario.getSenha()));
			usuario.setConfirmacaoSenha(usuario.getSenha());
		}
		
		//Correção de CPF
		if(!Pessoa.removerFormatoCPF(usuario.getPessoa().getCpf()).equals(usuarioBanco.getPessoa().getCpf()) && repository.existsByPessoaCpf(Pessoa.removerFormatoCPF(usuario.getPessoa().getCpf()))) {
			throw new CPFJaCadastradoException();
		}
		
		//Alteração/correção de E-mail
		Optional<Usuario> usuarioExistentePorEmail = repository.findByEmail(usuario.getEmail());
		if(usuarioExistentePorEmail.isPresent() && !usuarioExistentePorEmail.get().equals(usuario)) {
			throw new EmailUsuarioJaCadastradoException();
		}
		
		repository.save(usuario);
	}
}
