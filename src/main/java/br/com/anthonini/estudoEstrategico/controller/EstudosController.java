package br.com.anthonini.estudoEstrategico.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.anthonini.arquitetura.controller.AbstractController;
import br.com.anthonini.arquitetura.controller.page.PageWrapper;
import br.com.anthonini.estudoEstrategico.model.CicloEstudos;
import br.com.anthonini.estudoEstrategico.model.DiaEstudo;
import br.com.anthonini.estudoEstrategico.model.DiaPeriodoCicloEstudos;
import br.com.anthonini.estudoEstrategico.model.DisciplinaDiaEstudo;
import br.com.anthonini.estudoEstrategico.model.DisciplinaPeriodo;
import br.com.anthonini.estudoEstrategico.model.PeriodoCicloEstudos;
import br.com.anthonini.estudoEstrategico.model.Revisao;
import br.com.anthonini.estudoEstrategico.model.TipoRevisao;
import br.com.anthonini.estudoEstrategico.repository.helper.cicloEstudos.filter.CicloEstudosFilter;
import br.com.anthonini.estudoEstrategico.security.UsuarioSistema;
import br.com.anthonini.estudoEstrategico.service.CicloEstudosService;
import br.com.anthonini.estudoEstrategico.util.CargaHorariaUtil;

@Controller
@RequestMapping("/estudos")
public class EstudosController  extends AbstractController {
	
	private final CicloEstudosService cicloEstudosService;
	
	public EstudosController(CicloEstudosService cicloEsudosService) {
		this.cicloEstudosService = cicloEsudosService;
	}

	@GetMapping
	public ModelAndView listar(CicloEstudosFilter filter, @AuthenticationPrincipal UsuarioSistema usuarioSistema, HttpServletRequest httpServletRequest, @PageableDefault(size = 10) @SortDefault(value="nome") Pageable pageable) {
		ModelAndView mv = new ModelAndView("estudo/Ciclos");
		filter.setUsuario(usuarioSistema.getUsuario());
		PageWrapper<CicloEstudos> paginaWrapper = new PageWrapper<>(cicloEstudosService.filtrar(filter,pageable),httpServletRequest);
        mv.addObject("pagina", paginaWrapper);
		
		return mv;
	}
	
	@GetMapping("/{id}")
	public ModelAndView view(@PathVariable("id") Long id, @AuthenticationPrincipal UsuarioSistema usuarioSistema, ModelMap model, RedirectAttributes redirect) {
		CicloEstudos cicloEstudos = cicloEstudosService.findCicloEstudos(id);
		if (cicloEstudos == null || !usuarioSistema.getUsuario().equals(cicloEstudos.getUsuario())) {
            addMensagemErro(redirect, getMessage("ciclo-estudos.mensagem.naoEncontrado"));
            return new ModelAndView("redirect:/estudo");
        }

		model.addAttribute("diasEstudo", cicloEstudos.getDiasEstudo());
        return new ModelAndView("estudo/View");
    }

	private List<DiaEstudo> getDiasEstudo(CicloEstudos cicloEstudos) {
		List<DiaEstudo> diasEstudo = new ArrayList<>();
		int diaAtual = 0;
		
		for(PeriodoCicloEstudos periodo : cicloEstudos.getPeriodosCicloEstudos()) {
			for(int i = 1; i <= periodo.getDuracao(); i++) {
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
		
		return diasEstudo;
	}
}

