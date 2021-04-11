package br.com.anthonini.estudoEstrategico.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.anthonini.estudoEstrategico.model.CicloEstudos;
import br.com.anthonini.estudoEstrategico.model.Usuario;
import br.com.anthonini.estudoEstrategico.repository.CicloEstudosRepository;
import br.com.anthonini.estudoEstrategico.repository.helper.cicloEstudos.filter.CicloEstudosFilter;
import br.com.anthonini.estudoEstrategico.service.exception.NomeEntidadeJaCadastradaException;
import br.com.anthonini.estudoEstrategico.service.exception.UsuarioSemPermissaoParaRealizarEssaOperacao;

@Service
public class CicloEstudosService {

	@Autowired
	private CicloEstudosRepository repository;
	
	public Page<CicloEstudos> filtrar(CicloEstudosFilter filter, Pageable pageable) {
		return repository.filtrar(filter, pageable);
	}
	
	@Transactional
	public void salvar(CicloEstudos cicloEstudos, Usuario usuario) {
		cicloEstudos.setNome(cicloEstudos.getNome().trim());
		Optional<CicloEstudos> cicloEstudosOptional = repository.findByNomeIgnoreCaseAndUsuario(cicloEstudos.getNome(), usuario);
		
		if(cicloEstudosOptional.isPresent() && !cicloEstudos.equals(cicloEstudosOptional.get())) {
			throw new NomeEntidadeJaCadastradaException("Já existe um Ciclo de Estudos cadastrado com esse nome.");
		}
		
		if(cicloEstudos.isNovo()) {
			cicloEstudos.setUsuario(usuario);
		} else {
			CicloEstudos cicloEstudosBD = repository.getOne(cicloEstudos.getId());
			cicloEstudos.setUsuario(cicloEstudosBD.getUsuario());
			
			if(!usuario.equals(cicloEstudos.getUsuario())) {
				throw new UsuarioSemPermissaoParaRealizarEssaOperacao();
			}
		}
		
		repository.save(cicloEstudos);
	}
	
	@Transactional
	public void remover(CicloEstudos cicloEstudos) {
		repository.delete(cicloEstudos);
	}
}
