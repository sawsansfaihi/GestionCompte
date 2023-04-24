package tn.esprit.banque.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tn.esprit.banque.model.Compte;
import tn.esprit.banque.model.Compte.CategorieCompte;

@Repository
public interface CompteRepository extends JpaRepository<Compte, Long> {

	//@Query("select c from Compte c where  c.etatCompte = true ")
	List<Compte> findByCategorieCompte(CategorieCompte categorieCompte);

}
