package ma.enset.bankaccountservice.service;

import ma.enset.bankaccountservice.dto.CompteRequestDTO;
import ma.enset.bankaccountservice.dto.CompteResponseDTO;

import java.util.List;

/**
 * Couche métier du micro-service.
 */
public interface CompteService {
    List<CompteResponseDTO> getComptes();
    CompteResponseDTO getCompte(String id);
    CompteResponseDTO addCompte(CompteRequestDTO compteDTO);
    CompteResponseDTO updateCompte(String id, CompteRequestDTO compteDTO);
    void deleteCompte(String id);
}
