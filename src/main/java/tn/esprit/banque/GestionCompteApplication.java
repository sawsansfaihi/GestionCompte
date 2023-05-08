package tn.esprit.banque;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@Configuration
@EnableJpaRepositories(basePackages = "tn.esprit.banque.Repository.*")
@EntityScan("tn.esprit.banque.model.*")
@SpringBootApplication
@ComponentScan(basePackages = "tn.esprit.banque.Repository.*")
public class GestionCompteApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionCompteApplication.class, args);
	}
}
