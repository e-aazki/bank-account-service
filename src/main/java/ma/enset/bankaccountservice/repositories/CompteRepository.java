package ma.enset.bankaccountservice.repositories;

import ma.enset.bankaccountservice.entities.Compte;
import ma.enset.bankaccountservice.enums.TypeCompte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Repository Spring Data, exposé aussi comme API REST via Spring Data REST (/comptes).
 */
@RepositoryRestResource
public interface CompteRepository extends JpaRepository<Compte, String> {

    // http://localhost:8081/comptes/search/byType?t=COMPTE_COURANT
    @RestResource(path = "/byType")
    List<Compte> findByType(@Param("t") TypeCompte type);

    // http://localhost:8081/comptes/search/byClient?clientId=1
    @RestResource(path = "/byClient")
    List<Compte> findByClientId(@Param("clientId") Long clientId);
}
