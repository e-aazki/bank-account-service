package ma.enset.bankaccountservice.repositories;

import ma.enset.bankaccountservice.entities.Client;
import ma.enset.bankaccountservice.entities.Compte;
import ma.enset.bankaccountservice.enums.TypeCompte;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 4. Test de la couche DAO.
 */
@DataJpaTest
class CompteRepositoryTest {

    @Autowired
    private CompteRepository compteRepository;
    @Autowired
    private ClientRepository clientRepository;

    private Client client;

    @BeforeEach
    void setUp() {
        // on part d'une base vide (le CommandLineRunner de l'application insère des données de démo)
        compteRepository.deleteAll();
        clientRepository.deleteAll();
        client = clientRepository.save(Client.builder().nom("Test").build());
        compteRepository.save(nouveauCompte(1000.0, TypeCompte.COMPTE_COURANT));
        compteRepository.save(nouveauCompte(2000.0, TypeCompte.COMPTE_EPARGNE));
        compteRepository.save(nouveauCompte(3000.0, TypeCompte.COMPTE_COURANT));
    }

    private Compte nouveauCompte(double solde, TypeCompte type) {
        return Compte.builder()
                .id(UUID.randomUUID().toString())
                .dateCreation(new Date())
                .solde(solde)
                .devise("MAD")
                .type(type)
                .client(client)
                .build();
    }

    @Test
    void shouldSaveAndFindCompte() {
        Compte saved = compteRepository.save(nouveauCompte(500.0, TypeCompte.COMPTE_EPARGNE));
        Compte found = compteRepository.findById(saved.getId()).orElseThrow();
        assertThat(found.getSolde()).isEqualTo(500.0);
        assertThat(found.getClient().getNom()).isEqualTo("Test");
        assertThat(compteRepository.count()).isEqualTo(4);
    }

    @Test
    void shouldFindByType() {
        List<Compte> courants = compteRepository.findByType(TypeCompte.COMPTE_COURANT);
        assertThat(courants).hasSize(2).allMatch(c -> c.getType() == TypeCompte.COMPTE_COURANT);
    }

    @Test
    void shouldFindByClient() {
        assertThat(compteRepository.findByClientId(client.getId())).hasSize(3);
    }

    @Test
    void shouldUpdateAndDeleteCompte() {
        Compte compte = compteRepository.findByType(TypeCompte.COMPTE_EPARGNE).get(0);
        compte.setSolde(9999.0);
        compteRepository.save(compte);
        assertThat(compteRepository.findById(compte.getId()).orElseThrow().getSolde()).isEqualTo(9999.0);

        compteRepository.deleteById(compte.getId());
        assertThat(compteRepository.findById(compte.getId())).isEmpty();
    }
}
