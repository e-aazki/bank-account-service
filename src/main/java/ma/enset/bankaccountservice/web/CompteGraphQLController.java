package ma.enset.bankaccountservice.web;

import lombok.RequiredArgsConstructor;
import ma.enset.bankaccountservice.dto.CompteRequestDTO;
import ma.enset.bankaccountservice.dto.CompteResponseDTO;
import ma.enset.bankaccountservice.entities.Client;
import ma.enset.bankaccountservice.entities.Compte;
import ma.enset.bankaccountservice.exceptions.CompteNotFoundException;
import ma.enset.bankaccountservice.repositories.ClientRepository;
import ma.enset.bankaccountservice.repositories.CompteRepository;
import ma.enset.bankaccountservice.service.CompteService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Web service GraphQL (endpoint : /graphql, interface : /graphiql).
 */
@Controller
@RequiredArgsConstructor
public class CompteGraphQLController {

    private final CompteRepository compteRepository;
    private final ClientRepository clientRepository;
    private final CompteService compteService;

    @QueryMapping
    public List<Compte> comptesList() {
        return compteRepository.findAll();
    }

    @QueryMapping
    public Compte compteById(@Argument String id) {
        return compteRepository.findById(id).orElseThrow(() -> new CompteNotFoundException(id));
    }

    @QueryMapping
    public List<Client> clients() {
        return clientRepository.findAll();
    }

    /** Résolution du champ Client.comptes (évite le chargement lazy hors transaction). */
    @SchemaMapping(typeName = "Client", field = "comptes")
    public List<Compte> comptes(Client client) {
        return compteRepository.findByClientId(client.getId());
    }

    @MutationMapping
    public CompteResponseDTO addCompte(@Argument CompteRequestDTO compte) {
        return compteService.addCompte(compte);
    }

    @MutationMapping
    public CompteResponseDTO updateCompte(@Argument String id, @Argument CompteRequestDTO compte) {
        return compteService.updateCompte(id, compte);
    }

    @MutationMapping
    public Boolean deleteCompte(@Argument String id) {
        compteService.deleteCompte(id);
        return true;
    }
}
