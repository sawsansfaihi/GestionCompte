package tn.esprit.banque.service;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import tn.esprit.banque.exceptions.InvalidAmountException;
import tn.esprit.banque.exceptions.InvalidUserException;
import tn.esprit.banque.model.Compte;
import tn.esprit.banque.model.Compte.TypeCompte;
import tn.esprit.banque.repository.CompteRepository;

@Service
public abstract class CompteEpargne extends CompteAbstraction {

    private CompteRepository cptRepo;
    private RestTemplate restTemplate;

    @Autowired
    public void setCptDAO(CompteRepository cptRepo) {
        this.cptRepo = cptRepo;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Transactional
    public Compte createAccount(Compte compte, String utilisateurUrl) throws InvalidAmountException, InvalidUserException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

       ResponseEntity<Map> response = restTemplate.exchange(utilisateurUrl, HttpMethod.GET, entity, Map.class);
       Map utilisateur = response.getBody();

      
        compte.setDateCreation(new Date());
        compte.setEtatCompte(true);
        compte.setUtilisateur((String) utilisateur.get("nom"));
        TypeCompte typeCompte = Compte.TypeCompte.EPARGNE;
        compte.setTypeCompte(typeCompte);

        if (compte.getSoldeCompte().longValue() < 0) {
            throw new InvalidAmountException("Montant spécifié null et/ou négatif");
        }

        return cptRepo.save(compte);
    }

}
