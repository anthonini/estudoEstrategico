package br.com.anthonini.estudoEstrategico.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.anthonini.estudoEstrategico.model.Permissao;
import br.com.anthonini.estudoEstrategico.repository.PermissaoRepository;

@Service
public class PermissaoService {

	@Autowired
	private PermissaoRepository repository;
	
	public List<Permissao> findAll() {
		return repository.findAll();
	}
}
