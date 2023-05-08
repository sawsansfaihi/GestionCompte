package tn.esprit.banque.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tn.esprit.banque.model.Compte;
import tn.esprit.banque.model.Compte.CategorieCompte;

@Repository
public interface CompteRepository extends JpaRepository<Compte, Long> {

	@Query("select c from Compte c where  c.etatCompte = true ")
	List<Compte> findByCategorieCompte(CategorieCompte categorieCompte);

}
