package br.com.anthonini.estudoEstrategico.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.anthonini.arquitetura.controller.AbstractController;
import br.com.anthonini.arquitetura.controller.page.PageWrapper;
import br.com.anthonini.estudoEstrategico.dto.DisciplinaDiaEstudoDTO;
import br.com.anthonini.estudoEstrategico.dto.RevisaoDTO;
import br.com.anthonini.estudoEstrategico.model.CicloEstudos;
import br.com.anthonini.estudoEstrategico.repository.helper.cicloEstudos.filter.CicloEstudosFilter;
import br.com.anthonini.estudoEstrategico.security.UsuarioSistema;
import br.com.anthonini.estudoEstrategico.service.CicloEstudosService;
import br.com.anthonini.estudoEstrategico.service.EstudosService;
import br.com.anthonini.estudoEstrategico.service.exception.UsuarioSemPermissaoParaRealizarEssaOperacao;

@Controller
@RequestMapping("/estudos")
public class EstudosController  extends AbstractController {
	
	private final CicloEstudosService cicloEstudosService;
	
	private final EstudosService service;
	
	public EstudosController(EstudosService service, CicloEstudosService cicloEsudosService) {
		this.service = service;
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
            return new ModelAndView("redirect:/estudos");
        }

		model.addAttribute("diasEstudo", cicloEstudos.getDiasEstudo());
        return new ModelAndView("estudo/View");
    }
	
	@PutMapping("/atualizar-estudos-disciplina")
	public @ResponseBody ResponseEntity<?> atualizarDisciplina(DisciplinaDiaEstudoDTO dto, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		try {
			if(dto.getObservacao() != null && dto.getObservacao().trim().isEmpty())
				dto.setObservacao(null);
			this.service.atualizarEstudoDiaDisciplina(dto, usuarioSistema.getUsuario());
			return ResponseEntity.ok(dto);
		} catch (UsuarioSemPermissaoParaRealizarEssaOperacao e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/atualizar-estudos-revisao")
	public @ResponseBody ResponseEntity<?> atualizarRevisao(RevisaoDTO dto, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		try {
			if(dto.getObservacao() != null && dto.getObservacao().trim().isEmpty())
				dto.setObservacao(null);
			this.service.atualizarRevisao(dto, usuarioSistema.getUsuario());
			return ResponseEntity.ok(dto);
		} catch (UsuarioSemPermissaoParaRealizarEssaOperacao e) {
			return ResponseEntity.badRequest().build();
		}
	}
}

