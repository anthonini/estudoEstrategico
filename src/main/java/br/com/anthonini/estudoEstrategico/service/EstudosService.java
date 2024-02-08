package br.com.anthonini.estudoEstrategico.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.anthonini.estudoEstrategico.dto.DisciplinaDiaEstudoDTO;
import br.com.anthonini.estudoEstrategico.dto.RevisaoDTO;
import br.com.anthonini.estudoEstrategico.model.DisciplinaDiaEstudo;
import br.com.anthonini.estudoEstrategico.model.Revisao;
import br.com.anthonini.estudoEstrategico.model.Usuario;
import br.com.anthonini.estudoEstrategico.repository.DisciplinaDiaEstudoRepository;
import br.com.anthonini.estudoEstrategico.repository.RevisaoRespository;
import br.com.anthonini.estudoEstrategico.service.exception.UsuarioSemPermissaoParaRealizarEssaOperacao;

@Service
public class EstudosService {

	@Autowired
	private DisciplinaDiaEstudoRepository disciplinaDiaEstudoRepository;
	
	@Autowired
	private RevisaoRespository revisaoRespository;
	
	@Transactional
	public void atualizarEstudoDiaDisciplina(DisciplinaDiaEstudoDTO dto, Usuario usuario) {
		Optional<DisciplinaDiaEstudo> disciplinaDiaEstudoOptional = disciplinaDiaEstudoRepository.findById(dto.getId());
		
		if(disciplinaDiaEstudoOptional.isPresent()) {
			DisciplinaDiaEstudo disciplinaDiaEstudo = disciplinaDiaEstudoOptional.get();
			if(!usuario.equals(disciplinaDiaEstudo.getDiaEstudo().getCicloEstudos().getUsuario())) {
				throw new UsuarioSemPermissaoParaRealizarEssaOperacao();
			}
			
			if(dto.getPaginaFinal() != null && dto.getPaginaInicial() != null && dto.getPaginasEstudadas() == null ) {
				dto.setPaginasEstudadas(dto.getPaginaFinal()-dto.getPaginaInicial()+1);
			}
		
			try {
				BeanUtils.copyProperties(disciplinaDiaEstudo, dto);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	@Transactional
	public void atualizarRevisao(RevisaoDTO dto, Usuario usuario) {
		Optional<Revisao> revisaoOptional = revisaoRespository.findById(dto.getId());
		
		if(revisaoOptional.isPresent()) {
			Revisao revisao = revisaoOptional.get();
			if(!usuario.equals(revisao.getDiaEstudo().getCicloEstudos().getUsuario())) {
				throw new UsuarioSemPermissaoParaRealizarEssaOperacao();
			}
		
			try {
				BeanUtils.copyProperties(revisao, dto);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
}
