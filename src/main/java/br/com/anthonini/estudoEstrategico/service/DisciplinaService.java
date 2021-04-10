package br.com.anthonini.estudoEstrategico.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.anthonini.estudoEstrategico.model.Disciplina;
import br.com.anthonini.estudoEstrategico.model.Usuario;
import br.com.anthonini.estudoEstrategico.repository.DisciplinaRepository;
import br.com.anthonini.estudoEstrategico.repository.helper.disciplina.filter.DisciplinaFilter;
import br.com.anthonini.estudoEstrategico.service.exception.NomeDisciplinaJaCadastradaException;
import br.com.anthonini.estudoEstrategico.service.exception.UsuarioSemPermissaoParaRealizarEssaOperacao;

@Service
public class DisciplinaService {

	@Autowired
	private DisciplinaRepository repository;
	
	public Page<Disciplina> filtrar(DisciplinaFilter filter, Pageable pageable) {
		return repository.filtrar(filter, pageable);
	}

	@Transactional
	public void cadastrar(Disciplina disciplina, Usuario usuario) {
		disciplina.setNome(disciplina.getNome().trim());
		Optional<Disciplina> disciplinaOptional = repository.findByNomeIgnoreCaseAndUsuario(disciplina.getNome(), usuario);
		
		if(disciplinaOptional.isPresent() && !disciplina.equals(disciplinaOptional.get())) {
			throw new NomeDisciplinaJaCadastradaException();
		}
		
		if(disciplina.isNova()) {
			disciplina.setUsuario(usuario);
		} else {
			Disciplina disciplinaBD = repository.getOne(disciplina.getId());
			disciplina.setUsuario(disciplinaBD.getUsuario());
			
			if(!usuario.equals(disciplina.getUsuario())) {
				throw new UsuarioSemPermissaoParaRealizarEssaOperacao();
			}
		}
		
		repository.save(disciplina);
	}
}
