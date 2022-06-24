package br.com.anthonini.estudoEstrategico.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.anthonini.estudoEstrategico.model.GrupoUsuario;
import br.com.anthonini.estudoEstrategico.model.Usuario;
import br.com.anthonini.estudoEstrategico.repository.PermissaoUsuarioRepository;
import br.com.anthonini.estudoEstrategico.repository.UsuarioRepository;
import br.com.anthonini.estudoEstrategico.repository.helper.permissaousuario.filter.PermissaoUsuarioFilter;

@Service
public class PermissaoUsuarioService {

	@Autowired
	private PermissaoUsuarioRepository repository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public List<GrupoUsuario> buscarTodosGruposUsuario() {
		return repository.findAll();
	}

	public Page<Usuario> filtrar(PermissaoUsuarioFilter filter, Pageable pageable) {
		return repository.filtrar(filter, pageable);
	}

	@Transactional
	public void salvar(Usuario usuario) {
		Usuario usuarioBD = usuarioRepository.getOne(usuario.getId());
		
		if(usuarioBD != null) {
			usuarioBD.setGrupos(usuario.getGrupos());
			usuarioRepository.save(usuarioBD);
		}
	}
}
