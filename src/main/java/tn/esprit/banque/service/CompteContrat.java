package tn.esprit.banque.service;

import java.util.List;

import org.springframework.stereotype.Service;

import tn.esprit.banque.exceptions.InvalidAccountException;
import tn.esprit.banque.exceptions.InvalidAdminDeletionException;
import tn.esprit.banque.exceptions.InvalidConfirmationException;
import tn.esprit.banque.exceptions.InvalidHashPasswordException;
import tn.esprit.banque.exceptions.InvalidPasswordException;
import tn.esprit.banque.model.Compte;

public interface CompteContrat {


  

    Compte updateAccount(Compte compte, Long aId);

    List<Compte> allAccounts();

    Compte findLeCompte(Long id) throws InvalidAccountException;

    Compte disactivateAccount(Long idCompte,String motDePasse) throws InvalidAccountException, InvalidPasswordException, InvalidConfirmationException, InvalidAdminDeletionException, InvalidHashPasswordException;

}


