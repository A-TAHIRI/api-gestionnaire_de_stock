package com.tahiri.gestiondestock.service;

import com.tahiri.gestiondestock.exception.EntityNotFoundException;
import com.tahiri.gestiondestock.exception.ErrorCodes;
import com.tahiri.gestiondestock.exception.InvalidEntityException;
import com.tahiri.gestiondestock.exception.InvalidOperationException;
import com.tahiri.gestiondestock.model.*;
import com.tahiri.gestiondestock.repository.ArticleRepository;
import com.tahiri.gestiondestock.repository.LigneVenteRepository;
import com.tahiri.gestiondestock.repository.VenteRepository;
import com.tahiri.gestiondestock.validator.VenteValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class VenteService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private VenteRepository ventesRepository;
    @Autowired
    private LigneVenteRepository ligneVenteRepository;
    @Autowired
    private MvtStkService mvtStkService;



    public Vente save(Vente vente) {
        List<String> errors = VenteValidator.validate(vente);
        if (!errors.isEmpty()) {
            log.error("Ventes n'est pas valide");
            throw new InvalidEntityException("L'objet vente n'est pas valide", ErrorCodes.VENTE_NOT_VALID, errors);
        }

        List<String> articleErrors = new ArrayList<>();

        vente.getLigneVentes().forEach(ligneVenteDto -> {
            Optional<Article> article = articleRepository.findById(ligneVenteDto.getArticle().getId());
            if (article.isEmpty()) {
                articleErrors.add("Aucun article avec l'ID " + ligneVenteDto.getArticle().getId() + " n'a ete trouve dans la BDD");
            }
        });

        if (!articleErrors.isEmpty()) {
            log.error("One or more articles were not found in the DB, {}", errors);
            throw new InvalidEntityException("Un ou plusieurs articles n'ont pas ete trouve dans la BDD", ErrorCodes.VENTE_NOT_VALID, errors);
        }

        Vente savedVentes = ventesRepository.save(vente);

        vente.getLigneVentes().forEach(ligVte -> {
            LigneVente ligneVente = ligneVenteRepository.save(ligVte);
            ligneVente.setVente(savedVentes);
            ligneVenteRepository.save(ligneVente);
            updateMvtStk(ligneVente);
        });

        return ventesRepository.save(savedVentes);
    }

    public Vente getById(Integer id) {
        if (id == null) {
            log.error("Ventes ID is NULL");
            return null;
        }
        return ventesRepository.findById(id)

                .orElseThrow(() -> new EntityNotFoundException("Aucun vente n'a ete trouve dans la BDD", ErrorCodes.VENTE_NOT_FOUND));
    }
    public Vente getdByCode(String reference) {
        if (!StringUtils.hasLength(reference)) {
            log.error("Vente CODE is NULL");
            return null;
        }
        return ventesRepository.findByReference(reference)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune vente client n'a ete trouve avec le CODE " + reference, ErrorCodes.VENTE_NOT_VALID
                ));
    }

    public List<Vente> getdAll() {
        return ventesRepository.findAll();

    }
    public void delete(Integer id) {
        if (id == null) {
            log.error("Vente ID is NULL");
            return;
        }
        List<LigneVente> ligneVentes = ligneVenteRepository.findByVente_Id(id);
        if (!ligneVentes.isEmpty()) {
            throw new InvalidOperationException("Impossible de supprimer une vente ...",
                    ErrorCodes.VENTE_ALREADY_IN_USE);
        }
        ventesRepository.deleteById(id);
    }



    private void updateMvtStk(LigneVente lig) {
        MvtStk mvtStk = new MvtStk();
                mvtStk.setArticle(lig.getArticle());
                mvtStk.setDateMvt(Instant.now());
                mvtStk.setTypeMvt(TypeMvtStk.SORTIE);
                mvtStk.setSourceMvt(SourceMvtStk.VENTE);
                mvtStk.setQuantite(lig.getQuantite());
                mvtStk.setIdEntreprise(lig.getIdEntreprise());

        mvtStkService.sortieStock(mvtStk);
    }
}
