package ma.enset.bankaccountservice.entities;

import jakarta.persistence.*;
import lombok.*;
import ma.enset.bankaccountservice.enums.TypeCompte;

import java.util.Date;

/**
 * Entité JPA représentant un compte bancaire.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compte {
    @Id
    private String id;
    private Date dateCreation;
    private Double solde;
    private String devise;
    @Enumerated(EnumType.STRING)
    private TypeCompte type;
    @ManyToOne
    private Client client;
}
