package ma.enset.bankaccountservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.enset.bankaccountservice.enums.TypeCompte;

/**
 * DTO reçu en entrée (création / mise à jour d'un compte).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompteRequestDTO {
    private Double solde;
    private String devise;
    private TypeCompte type;
}
