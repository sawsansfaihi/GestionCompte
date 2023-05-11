package tn.esprit.banque.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import jakarta.validation.Valid;
import tn.esprit.banque.exceptions.InvalidAccountException;
import tn.esprit.banque.exceptions.InvalidAdminDeletionException;
import tn.esprit.banque.exceptions.InvalidAmountException;
import tn.esprit.banque.exceptions.InvalidConfirmationException;
import tn.esprit.banque.exceptions.InvalidHashPasswordException;
import tn.esprit.banque.exceptions.InvalidPasswordException;
import tn.esprit.banque.exceptions.InvalidUserException;
import tn.esprit.banque.model.Compte;
import tn.esprit.banque.model.Compte.CategorieCompte;
import tn.esprit.banque.repository.CompteRepository;
import tn.esprit.banque.service.CompteContrat;
import tn.esprit.banque.service.CompteCourant;
import tn.esprit.banque.service.CompteCreation;
import tn.esprit.banque.service.CompteEpargne;

@RestController
public class CompteController {

	@Autowired
	private CompteContrat compteContrat;
	
	@Autowired
	private CompteRepository CompteRepo;

	@Autowired
	CompteCourant ct;

	@Autowired
	CompteEpargne ep;
	@Autowired
	private RestTemplate restTemplate;

	@PostMapping(value = "/AccountCreation", produces = "application/json", consumes = "application/json")
	public ResponseEntity<Object> creationCompte(@Valid @RequestBody CompteCreation cmpt, BindingResult bindingResult) {
		Compte compte = new Compte();
		try {
			if (!cmpt.getMotDePasse().equals(cmpt.getReMotDePasse())) {
				throw new InvalidPasswordException("Veuillez re saisir le meme mot de passe dans le champ reMotDePasse Pour la confirmation ");
			}
			compte.setMotDePasse(cmpt.getMotDePasse());
			compte.setSoldeCompte(cmpt.getSoldeCompte());
			if (compte.getSoldeCompte().doubleValue() > 5000) {
				compte.setCategorieCompte(CategorieCompte.PLATINUM);
			} else if (compte.getSoldeCompte().doubleValue() > 2000) {
				compte.setCategorieCompte(CategorieCompte.GOLD);
			} else {
				compte.setCategorieCompte(CategorieCompte.SILVER);
			}
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer ");
			HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
			ResponseEntity<Map> userResponse = restTemplate.exchange("http://localhost:8080/api/users/me", HttpMethod.GET, entity, Map.class);
			Map utilisateur = userResponse.getBody();
			switch (cmpt.getTypecompte()) {
			case "COURANT":
				return new ResponseEntity<>(ct.createAccount(compte, "user1"), HttpStatus.OK);
			case "EPARGNE":
				return new ResponseEntity<>(ep.createAccount(compte, "user2"), HttpStatus.OK);
			default:
				throw new InvalidAccountException("Entrez un type du compte valide en majiscule ex: 'COURANT' 'EPARGNE' ");
			}
		} catch (InvalidAmountException | InvalidUserException | InvalidAccountException | InvalidPasswordException e) {
			Map<String, String> error = new HashMap<>();
			if (bindingResult.hasErrors()) {
				for (FieldError fd : bindingResult.getFieldErrors()) {
					error.put(fd.getField(), fd.getDefaultMessage());
				}
				error.put("message", e.toString());
				return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
			} else {
				error.put("message", e.toString());
				return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
			}
		}
	}

	@GetMapping(value = "/getAccount/{numer_compte}", produces = { "application/json" })
	public ResponseEntity<Object> chargerLeCompte(@PathVariable("numer_compte") Long numer_compte) {

		Map<String, String> error = new HashMap<>();

		try {

			Compte compte = compteContrat.findLeCompte(numer_compte);
			return new ResponseEntity<>(compte, HttpStatus.OK);

		} catch (Exception e) {
			error.put("Compte", "Compte Introuvable " + e);
			return new ResponseEntity<Object>(error, HttpStatus.NOT_ACCEPTABLE);
		}

	}

	@PostMapping(value = "/deleteAccount", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> supprimerLeCompte(@Valid @RequestBody Compte compte, BindingResult bindingResult) {

		try {

			return new ResponseEntity<>(
					compteContrat.disactivateAccount(compte.getNumeroCompte(), compte.getMotDePasse()), HttpStatus.OK);

		} catch (InvalidAccountException | InvalidPasswordException | InvalidConfirmationException
				| InvalidAdminDeletionException | InvalidHashPasswordException e) {

			Map<String, String> error = new HashMap<>();

			if (bindingResult.hasErrors()) {

				for (FieldError fd : bindingResult.getFieldErrors()) {
					error.put(fd.getField(), fd.getDefaultMessage());
				}

				error.put("message", e.toString());
				return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);

			} else {
				error.put("message", e.toString());
				return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
			}

		}

	}

	@PutMapping(value = "/updateAccount", produces = "application/json", consumes = "application/json")
	public ResponseEntity<Object> updateAccount(@Valid @RequestBody Compte updatedAccount)
			throws InvalidAccountException {
		if (CompteRepo.existsById(updatedAccount.getNumeroCompte())) {
			CompteRepo.save(updatedAccount);
			return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
		}
		throw new InvalidAccountException("Compte indisponible");
	}

	@GetMapping(value = "/accounts/{category}", produces = { "application/json" })
	public ResponseEntity<Object> getAccountsByCategory(@PathVariable("category") String category) {
		List<Compte> comptes = CompteRepo.findByCategorieCompte(CategorieCompte.valueOf(category));
		for (Compte c : comptes) {
			if (!c.isEtatCompte()) {
				comptes.remove(c);
			}
		}
		return new ResponseEntity<Object>(comptes, HttpStatus.OK);

	}

}
