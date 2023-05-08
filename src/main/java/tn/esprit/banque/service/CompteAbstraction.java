package tn.esprit.banque.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import tn.esprit.banque.exceptions.InvalidAmountException;
import tn.esprit.banque.exceptions.InvalidUserException;
import tn.esprit.banque.model.Compte;
import tn.esprit.banque.model.Compte.TypeCompte;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Service
public abstract  class CompteAbstraction {
	TypeCompte typeCompte;


	// Implementation of createAccount method
	public Compte createAccount(Compte compte, String utilisateurUrl) throws InvalidAmountException, InvalidUserException {
	    // Make an HTTP request to the user microservice to retrieve the currently logged in user
	    RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders headers = new HttpHeaders();
	    headers.set("Authorization", "Bearer dktfghlfkhgi"); // Set the Authorization header to include the access token
	   HttpEntity<String> entity = new HttpEntity<>(null, headers);
	    ResponseEntity<Map> response = restTemplate.exchange(utilisateurUrl, HttpMethod.GET, entity, Map.class);
	    if (response.getStatusCode() != HttpStatus.OK) {
	        throw new InvalidUserException(utilisateurUrl);
	    }
	    Map utilisateur = response.getBody();

	    // Create the account
	    compte.setUtilisateur((String) utilisateur.get("nom"));
	    BigDecimal d = new BigDecimal(0.00) ;
	    compte.setSoldeCompte(d);
	    compte.setDateCreation(new Date());

	    // Save the account to the database
	    save(compte);

	    // Return the created account
	    return compte;
	}


	protected abstract void save(Compte compte);

	


}
