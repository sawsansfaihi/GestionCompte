package tn.esprit.banque.model;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Compte {
	public enum TypeCompte {
		EPARGNE, COURANT;
	}

	public enum CategorieCompte {
		SILVER, GOLD, PLATINUM;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	private Long numeroCompte;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private boolean etatCompte;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Temporal(TemporalType.DATE)
	private Date dateCreation;

	@DecimalMin(value = "0.0", message = "Veuillez specifier un solde superieure ou egale à zero")
	private BigDecimal soldeCompte;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotBlank(message = "Veuillez saisir un mot de passe")
	private String motDePasse;

	@NotNull(message = "Veuillez preciser le type du compte")
	@Enumerated(EnumType.STRING)
	private TypeCompte typeCompte;

	@Enumerated(EnumType.STRING)
	private CategorieCompte categorieCompte;
	
	/**
	 * Constructeur de l'entité Compte
	 * 
	 * @param numeroCompte
	 * @param etatCompte
	 * @param dateCreation
	 * @param soldeCompte
	 * @param typeCompte
	 * @param motDePasse
	 */

	public Compte(Long numeroCompte, boolean etatCompte, Date dateCreation, BigDecimal soldeCompte,
			TypeCompte typeCompte, String motDePasse) {
		this.numeroCompte = numeroCompte;
		this.etatCompte = etatCompte;
		this.dateCreation = dateCreation;
		this.soldeCompte = soldeCompte;
		this.typeCompte = typeCompte;
		this.motDePasse = motDePasse;
	}

	public Compte() {
	}

	public Long getNumeroCompte() {
		return numeroCompte;
	}

	public CategorieCompte getCategorieCompte() {
		return categorieCompte;
	}

	public void setCategorieCompte(CategorieCompte categorieCompte) {
		this.categorieCompte = categorieCompte;
	}

	public void setNumeroCompte(Long numeroCompte) {
		this.numeroCompte = numeroCompte;
	}

	public boolean isEtatCompte() {
		return etatCompte;
	}

	public void setEtatCompte(boolean etatCompte) {
		this.etatCompte = etatCompte;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public BigDecimal getSoldeCompte() {
		return soldeCompte;
	}

	public void setSoldeCompte(@DecimalMin(value = "0.0", message = "Veuillez specifier un solde superieure ou egale à zero") BigDecimal d) {
		this.soldeCompte = d;
	}

	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}

	public TypeCompte getTypeCompte() {
		return typeCompte;
	}

	public void setTypeCompte(TypeCompte typeCompte) {
		this.typeCompte = typeCompte;
	}

	/**
	 * @return Chaines de caracteres des infos sur Compte
	 */

	@Override
	public String toString() {
		return "Compte{" + "numeroCompte=" + numeroCompte + ", etatCompte=" + etatCompte + ", dateCreation="
				+ dateCreation + ", soldeCompte=" + soldeCompte + ", motDePasse='" + motDePasse + '\'' + ", typeCompte="
				+ typeCompte + '}';
	}
}
