package ma.enset.bankaccountservice.service;

import lombok.RequiredArgsConstructor;
import ma.enset.bankaccountservice.dto.CompteRequestDTO;
import ma.enset.bankaccountservice.dto.CompteResponseDTO;
import ma.enset.bankaccountservice.entities.Compte;
import ma.enset.bankaccountservice.exceptions.CompteNotFoundException;
import ma.enset.bankaccountservice.mappers.CompteMapper;
import ma.enset.bankaccountservice.repositories.CompteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CompteServiceImpl implements CompteService {

    private final CompteRepository compteRepository;
    private final CompteMapper compteMapper;

    @Override
    public List<CompteResponseDTO> getComptes() {
        return compteRepository.findAll().stream().map(compteMapper::fromCompte).toList();
    }

    @Override
    public CompteResponseDTO getCompte(String id) {
        return compteMapper.fromCompte(findCompte(id));
    }

    @Override
    public CompteResponseDTO addCompte(CompteRequestDTO compteDTO) {
        Compte compte = Compte.builder()
                .id(UUID.randomUUID().toString())
                .dateCreation(new Date())
                .solde(compteDTO.getSolde())
                .devise(compteDTO.getDevise())
                .type(compteDTO.getType())
                .build();
        return compteMapper.fromCompte(compteRepository.save(compte));
    }

    @Override
    public CompteResponseDTO updateCompte(String id, CompteRequestDTO compteDTO) {
        Compte compte = findCompte(id);
        // mise à jour partielle : seuls les champs fournis sont modifiés
        if (compteDTO.getSolde() != null) compte.setSolde(compteDTO.getSolde());
        if (compteDTO.getDevise() != null) compte.setDevise(compteDTO.getDevise());
        if (compteDTO.getType() != null) compte.setType(compteDTO.getType());
        return compteMapper.fromCompte(compteRepository.save(compte));
    }

    @Override
    public void deleteCompte(String id) {
        compteRepository.delete(findCompte(id));
    }

    private Compte findCompte(String id) {
        return compteRepository.findById(id).orElseThrow(() -> new CompteNotFoundException(id));
    }
}
