package ma.enset.bankaccountservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.enset.bankaccountservice.enums.TypeCompte;

import java.util.Date;

/**
 * DTO renvoyé au client de l'API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompteResponseDTO {
    private String id;
    private Date dateCreation;
    private Double solde;
    private String devise;
    private TypeCompte type;
}
