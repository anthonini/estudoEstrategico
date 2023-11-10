package br.com.anthonini.estudoEstrategico.controller;

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
import br.com.anthonini.estudoEstrategico.repository.helper.cicloEstudos.filter.CicloEstudosFilter;
import br.com.anthonini.estudoEstrategico.security.UsuarioSistema;
import br.com.anthonini.estudoEstrategico.service.CicloEstudosService;
import br.com.anthonini.estudoEstrategico.service.EstudosService;

@Controller
@RequestMapping("/professor")
public class ProfessorController  extends AbstractController {
	
	private final CicloEstudosService cicloEstudosService;
	
	public ProfessorController(EstudosService service, CicloEstudosService cicloEsudosService) {
		this.cicloEstudosService = cicloEsudosService;
	}

	@GetMapping("/aluno-ciclo-estudos")
	public ModelAndView listar(CicloEstudosFilter filter, @AuthenticationPrincipal UsuarioSistema usuarioSistema, HttpServletRequest httpServletRequest, @PageableDefault(size = 10) @SortDefault(value={"nome"}) Pageable pageable) {
		ModelAndView mv = new ModelAndView("professor/Ciclos");
		filter.setUsuarioProfessor(usuarioSistema.getUsuario());
		PageWrapper<CicloEstudos> paginaWrapper = new PageWrapper<>(cicloEstudosService.filtrar(filter,pageable),httpServletRequest);
        mv.addObject("pagina", paginaWrapper);
		
		return mv;
	}
	
	@GetMapping("/aluno-ciclo-estudos/{id}")
	public ModelAndView view(@PathVariable("id") Long id, ModelMap model, RedirectAttributes redirect) {
		CicloEstudos cicloEstudos = cicloEstudosService.findCicloEstudos(id);
		if (cicloEstudos == null) {
            addMensagemErro(redirect, getMessage("ciclo-estudos.mensagem.naoEncontrado"));
            return new ModelAndView("redirect:/professor/ciclo-estudos");
        }

		model.addAttribute("cicloEstudos", cicloEstudos);
		model.addAttribute("diasEstudo", cicloEstudos.getDiasEstudo());
        return new ModelAndView("professor/ViewCiclo");
    }
}

