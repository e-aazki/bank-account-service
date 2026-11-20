package ma.enset.bankaccountservice;

import ma.enset.bankaccountservice.entities.Client;
import ma.enset.bankaccountservice.entities.Compte;
import ma.enset.bankaccountservice.enums.TypeCompte;
import ma.enset.bankaccountservice.repositories.ClientRepository;
import ma.enset.bankaccountservice.repositories.CompteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class BankAccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankAccountServiceApplication.class, args);
    }

    /** Jeu de données initial. */
    @Bean
    CommandLineRunner start(CompteRepository compteRepository, ClientRepository clientRepository) {
        return args -> {
            List.of("Mohamed", "Yassine", "Hanae", "Imane").forEach(nom ->
                    clientRepository.save(Client.builder().nom(nom).build()));

            clientRepository.findAll().forEach(client -> {
                for (int i = 0; i < 5; i++) {
                    Compte compte = Compte.builder()
                            .id(UUID.randomUUID().toString())
                            .type(Math.random() > 0.5 ? TypeCompte.COMPTE_COURANT : TypeCompte.COMPTE_EPARGNE)
                            .solde(10000 + Math.random() * 90000)
                            .dateCreation(new Date())
                            .devise("MAD")
                            .client(client)
                            .build();
                    compteRepository.save(compte);
                }
            });
        };
    }
}
