package ma.enset.bankaccountservice.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.enset.bankaccountservice.dto.CompteRequestDTO;
import ma.enset.bankaccountservice.dto.CompteResponseDTO;
import ma.enset.bankaccountservice.exceptions.CompteNotFoundException;
import ma.enset.bankaccountservice.service.CompteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Web service RESTful de gestion des comptes.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Comptes", description = "Gestion des comptes bancaires")
public class CompteRestController {

    private final CompteService compteService;

    @GetMapping("/comptes")
    @Operation(summary = "Lister tous les comptes")
    public List<CompteResponseDTO> comptes() {
        return compteService.getComptes();
    }

    @GetMapping("/comptes/{id}")
    @Operation(summary = "Consulter un compte par son identifiant")
    public CompteResponseDTO compte(@PathVariable String id) {
        return compteService.getCompte(id);
    }

    @PostMapping("/comptes")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer un nouveau compte")
    public CompteResponseDTO save(@RequestBody CompteRequestDTO requestDTO) {
        return compteService.addCompte(requestDTO);
    }

    @PutMapping("/comptes/{id}")
    @Operation(summary = "Mettre à jour un compte")
    public CompteResponseDTO update(@PathVariable String id, @RequestBody CompteRequestDTO requestDTO) {
        return compteService.updateCompte(id, requestDTO);
    }

    @DeleteMapping("/comptes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un compte")
    public void delete(@PathVariable String id) {
        compteService.deleteCompte(id);
    }

    @ExceptionHandler(CompteNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleNotFound(CompteNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
