package br.com.anthonini.estudoEstrategico.service;

import java.util.Optional;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.anthonini.estudoEstrategico.model.GrupoUsuario;
import br.com.anthonini.estudoEstrategico.repository.GrupoUsuarioRepository;
import br.com.anthonini.estudoEstrategico.repository.helper.grupousuario.filter.GrupoUsuarioFilter;
import br.com.anthonini.estudoEstrategico.service.exception.NaoEPossivelRemoverEntidadeException;
import br.com.anthonini.estudoEstrategico.service.exception.NomeEntidadeJaCadastradaException;

@Service
public class GrupoUsuarioService {

	@Autowired
	private GrupoUsuarioRepository repository;
	
	public Page<GrupoUsuario> filtrar(GrupoUsuarioFilter filter, Pageable pageable) {
		return repository.filtrar(filter, pageable);
	}
	
	@Transactional
	public void cadastrar(GrupoUsuario grupoUsuario) {
		grupoUsuario.setNome(grupoUsuario.getNome().trim());
		Optional<GrupoUsuario> grupoUsuarioOptional = repository.findByNomeIgnoreCase(grupoUsuario.getNome());
		
		if(grupoUsuarioOptional.isPresent() && !grupoUsuario.equals(grupoUsuarioOptional.get())) {
			throw new NomeEntidadeJaCadastradaException("Já existe um grupo de usuário cadastrado com esse nome.");
		}
		
		repository.save(grupoUsuario);
	}

	@Transactional
	public void remover(GrupoUsuario grupoUsuario) {
		try {
			repository.delete(grupoUsuario);
			repository.flush();
		} catch (PersistenceException | DataIntegrityViolationException e) {
			throw new NaoEPossivelRemoverEntidadeException("Não é possivel remover o Grupo de Usuário " + grupoUsuario.getNome() + ".");
		}
	}
}
