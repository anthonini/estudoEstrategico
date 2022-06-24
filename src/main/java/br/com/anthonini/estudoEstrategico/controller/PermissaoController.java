package br.com.anthonini.estudoEstrategico.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.anthonini.arquitetura.controller.page.PageWrapper;
import br.com.anthonini.estudoEstrategico.model.CicloEstudos;
import br.com.anthonini.estudoEstrategico.repository.helper.cicloEstudos.filter.CicloEstudosFilter;
import br.com.anthonini.estudoEstrategico.security.UsuarioSistema;

@Controller
@RequestMapping("/permissao")
public class PermissaoController {

	@PreAuthorize("hasRole('ROLE_CADASTRAR_PERMISSOES')")
	@GetMapping
	public ModelAndView listar(CicloEstudosFilter filter, @AuthenticationPrincipal UsuarioSistema usuarioSistema, HttpServletRequest httpServletRequest, @PageableDefault(size = 10) @SortDefault(value="nome") Pageable pageable) {
		ModelAndView mv = new ModelAndView("estudo/Ciclos");
		filter.setUsuario(usuarioSistema.getUsuario());
		PageWrapper<CicloEstudos> paginaWrapper = new PageWrapper<>(new PageImpl<>(new ArrayList<>()),httpServletRequest);
        mv.addObject("pagina", paginaWrapper);
		
		return mv;
	}
}
