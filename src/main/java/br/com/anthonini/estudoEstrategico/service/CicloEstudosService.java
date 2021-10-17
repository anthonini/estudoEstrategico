package br.com.anthonini.estudoEstrategico.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.anthonini.estudoEstrategico.model.CicloEstudos;
import br.com.anthonini.estudoEstrategico.model.DiaEstudo;
import br.com.anthonini.estudoEstrategico.model.DiaPeriodoCicloEstudos;
import br.com.anthonini.estudoEstrategico.model.DisciplinaDiaEstudo;
import br.com.anthonini.estudoEstrategico.model.DisciplinaPeriodo;
import br.com.anthonini.estudoEstrategico.model.PeriodoCicloEstudos;
import br.com.anthonini.estudoEstrategico.model.Revisao;
import br.com.anthonini.estudoEstrategico.model.TipoRevisao;
import br.com.anthonini.estudoEstrategico.model.Usuario;
import br.com.anthonini.estudoEstrategico.repository.CicloEstudosRepository;
import br.com.anthonini.estudoEstrategico.repository.helper.cicloEstudos.filter.CicloEstudosFilter;
import br.com.anthonini.estudoEstrategico.service.exception.NomeEntidadeJaCadastradaException;
import br.com.anthonini.estudoEstrategico.service.exception.UsuarioSemPermissaoParaRealizarEssaOperacao;
import br.com.anthonini.estudoEstrategico.util.CargaHorariaUtil;

@Service
public class CicloEstudosService {

	@Autowired
	private CicloEstudosRepository repository;
	
	public Page<CicloEstudos> filtrar(CicloEstudosFilter filter, Pageable pageable) {
		return repository.filtrar(filter, pageable);
	}
	
	public CicloEstudos findCicloEstudos(Long id) {
		CicloEstudos cicloEstudos = repository.findCicloEstudos(id);
		for(DiaEstudo diaEstudo : cicloEstudos.getDiasEstudo()) {
			diaEstudo.getRevisoes().iterator();
			diaEstudo.getDisciplinas().iterator();
		}
		return cicloEstudos;
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
			if(!usuario.equals(cicloEstudos.getUsuario())) {
				throw new UsuarioSemPermissaoParaRealizarEssaOperacao();
			}
		}
		
		ajustarDiasEstudo(cicloEstudos);
		
		repository.save(cicloEstudos);
	}

	@Transactional
	public void remover(CicloEstudos cicloEstudos) {
		repository.delete(cicloEstudos);
	}
	
	private void ajustarDiasEstudo(CicloEstudos cicloEstudos) {
		List<DiaEstudo> diasEstudo = cicloEstudos.getDiasEstudo();
		int diaAtual = diasEstudo.size();
		
		while(diaAtual > 0 && !diasEstudo.get(diaAtual-1).isEstudoIniciado()) {
			diaAtual--;
		}
		
		diasEstudo.retainAll(diasEstudo.subList(0, diaAtual));
		
		for(PeriodoCicloEstudos periodo : cicloEstudos.getPeriodosCicloEstudos()) {
			for(int dia = 1; dia <= periodo.getDuracao(); dia++) {
				diaAtual++;
				DiaPeriodoCicloEstudos diaPeriodo = DiaPeriodoCicloEstudos.getDiaPeriodo(diaAtual);
				List<DisciplinaPeriodo> disciplinasPeriodo = diaPeriodo.getDisciplinas(periodo);
				
				DiaEstudo diaEstudo = new DiaEstudo();
				diaEstudo.setDia(diaAtual);
				diaEstudo.setCicloEstudos(cicloEstudos);
				
				for(TipoRevisao tipoRevisao : TipoRevisao.values()) {
					Revisao revisao = tipoRevisao.getRevisao(diasEstudo, diaEstudo);
					if(revisao != null) {
						diaEstudo.getRevisoes().add(revisao);
					}
				}
				
				Integer cargaHorariaRevisaoPorDisciplina = diaEstudo.getCargaHorariaRevisao()/disciplinasPeriodo.size();
				
				for(DisciplinaPeriodo disciplinaPeriodo : disciplinasPeriodo) {
					DisciplinaDiaEstudo disciplinaDia = new DisciplinaDiaEstudo();
					disciplinaDia.setDisciplina(disciplinaPeriodo.getDisciplina());
					disciplinaDia.setDiaEstudo(diaEstudo);
					disciplinaDia.setCargaHoraria(CargaHorariaUtil.getCargaHorariaDisciplina(disciplinaPeriodo.getCargaHoraria(), cargaHorariaRevisaoPorDisciplina));
					disciplinaDia.setOrdem(disciplinaPeriodo.getOrdem());
					diaEstudo.getDisciplinas().add(disciplinaDia);
				}
				
				diasEstudo.add(diaEstudo);
			}
		}
	}
}
