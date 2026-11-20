package ma.enset.bankaccountservice.entities;

import ma.enset.bankaccountservice.enums.TypeCompte;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.util.Date;

/**
 * Projection plus complète incluant le nom du client :
 * http://localhost:8081/comptes?projection=details
 */
@Projection(types = Compte.class, name = "details")
public interface CompteDetailsProjection {
    String getId();
    Date getDateCreation();
    Double getSolde();
    String getDevise();
    TypeCompte getType();
    @Value("#{target.client != null ? target.client.nom : null}")
    String getNomClient();
}
