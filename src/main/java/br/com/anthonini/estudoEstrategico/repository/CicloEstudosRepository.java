package br.com.anthonini.estudoEstrategico.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.anthonini.estudoEstrategico.model.CicloEstudos;
import br.com.anthonini.estudoEstrategico.model.Usuario;
import br.com.anthonini.estudoEstrategico.repository.helper.cicloEstudos.CicloEstudosRepositoryQueries;

@Repository
public interface CicloEstudosRepository extends JpaRepository<CicloEstudos, Long>, CicloEstudosRepositoryQueries {

	Optional<CicloEstudos> findByNomeIgnoreCaseAndUsuario(String nome, Usuario usuario);
	
	@Query("   SELECT c "
			+ "FROM CicloEstudos c "
			+ "JOIN FETCH c.periodosCicloEstudos p "
			+ "WHERE c.id = :id")
	public CicloEstudos findCicloEstudos(@Param("id") Long id);
}
