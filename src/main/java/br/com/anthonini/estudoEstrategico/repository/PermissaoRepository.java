package br.com.anthonini.estudoEstrategico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.anthonini.estudoEstrategico.model.Permissao;

@Repository
public interface PermissaoRepository extends JpaRepository<Permissao, Long> {

}
