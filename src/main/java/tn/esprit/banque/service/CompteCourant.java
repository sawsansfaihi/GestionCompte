package tn.esprit.banque.service;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import tn.esprit.banque.exceptions.InvalidAmountException;
import tn.esprit.banque.exceptions.InvalidUserException;
import tn.esprit.banque.model.Compte;
import tn.esprit.banque.model.Compte.TypeCompte;
import tn.esprit.banque.repository.CompteRepository;

@Service
public abstract class CompteCourant extends CompteAbstraction {

	@Autowired
	private CompteRepository cptRepo;

	@Autowired
	private RestTemplate restTemplate;

	private static final String UTILISATEUR_URL = "http://localhost:8080/api/users/me";

	@Transactional
	public Compte createAccount(Compte compte) throws InvalidAmountException, InvalidUserException {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<>(headers);

		//ResponseEntity<Utilisateur> response = restTemplate.exchange(UTILISATEUR_URL, HttpMethod.GET, entity,Utilisateur.class);
		//Utilisateur utilisateur = response.getBody();

		compte.setDateCreation(new Date());
		compte.setEtatCompte(true);
		//compte.setUtilisateur(utilisateur);
		TypeCompte typeCompte = Compte.TypeCompte.COURANT;
		compte.setTypeCompte(typeCompte);

		if (compte.getSoldeCompte().compareTo(BigDecimal.ZERO) < 0) {
			throw new InvalidAmountException("Montant spécifié null et/ou négatif");
		}

		return cptRepo.save(compte);
	}

}
