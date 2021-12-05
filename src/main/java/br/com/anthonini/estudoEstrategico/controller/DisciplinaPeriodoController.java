package br.com.anthonini.estudoEstrategico.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.com.anthonini.arquitetura.controller.AbstractController;
import br.com.anthonini.estudoEstrategico.model.DiaPeriodoCicloEstudos;
import br.com.anthonini.estudoEstrategico.model.Disciplina;
import br.com.anthonini.estudoEstrategico.model.DisciplinaPeriodo;
import br.com.anthonini.estudoEstrategico.model.PeriodoCicloEstudos;
import br.com.anthonini.estudoEstrategico.service.DisciplinaService;
import br.com.anthonini.estudoEstrategico.sessao.PeriodoCicloEstudosSessao;

@Controller
@RequestMapping("/disciplina-periodo")
public class DisciplinaPeriodoController extends AbstractController {
	
	@Autowired
	private PeriodoCicloEstudosSessao sessao;
	
	@Autowired
	private DisciplinaService disciplinaService;
	
	@PutMapping
	public ModelAndView modal(DisciplinaPeriodo disciplinaPeriodo, ModelMap model, String uuid, DiaPeriodoCicloEstudos dia) {
		ModelAndView mv = new ModelAndView("periodo-ciclo-estudos/fragments/disciplinaPeriodoModal");
		mv.addObject("uuid", uuid);
		mv.addObject("dia", dia);
		
		return mv;
	}
	
	@PostMapping("/adicionar")
	public ModelAndView adicionar(@Valid DisciplinaPeriodo disciplinaPeriodo, BindingResult bindingResult, ModelMap model, String uuid, DiaPeriodoCicloEstudos dia) {
		if(bindingResult.hasErrors()) {
			addMensagensErroValidacao(model, bindingResult);
		} else {
			PeriodoCicloEstudos periodo = sessao.getPeriodoCicloEstudos(uuid);
			disciplinaPeriodo.setPeriodoCicloEstudos(periodo);
			disciplinaPeriodo.setOrdem(dia.getDisciplinas(periodo).size()+1);
			periodo.getDisciplinas().add(disciplinaPeriodo);
			
			model.addAttribute("disciplinaAdicionada", true);
			model.addAttribute("dia", dia);
			model.addAttribute("disciplinaPeriodo", new DisciplinaPeriodo());
			addMensagemSucesso(model, "Disciplina adicionada com sucesso!");
		}
		
		return modal(null, model, uuid, dia);
	}
	
	@DeleteMapping("/remover/{index}")
	public @ResponseBody ResponseEntity<?> remover(@PathVariable Integer index, String uuid, DiaPeriodoCicloEstudos dia, ModelMap model) {
		PeriodoCicloEstudos periodoCicloEstudos = sessao.getPeriodoCicloEstudos(uuid);
		
		if(index >= 0 && index < periodoCicloEstudos.getDisciplinas().size())
			dia.removerDisciplina(periodoCicloEstudos, index);
		
		return ResponseEntity.ok().build();
	}
	
	@ModelAttribute("disciplinasUsuario")
	public List<Disciplina> getDisciplinasUsuario() {
		return disciplinaService.disciplinasUsuario(getUsuarioLogado());
	}
}
