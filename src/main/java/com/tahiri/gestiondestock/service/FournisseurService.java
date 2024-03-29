package com.tahiri.gestiondestock.service;


import com.tahiri.gestiondestock.exception.EntityNotFoundException;
import com.tahiri.gestiondestock.exception.ErrorCodes;
import com.tahiri.gestiondestock.exception.InvalidEntityException;
import com.tahiri.gestiondestock.exception.InvalidOperationException;
import com.tahiri.gestiondestock.model.CommandeFournisseur;
import com.tahiri.gestiondestock.model.Fournisseur;
import com.tahiri.gestiondestock.repository.CommandeFournisseurRepository;
import com.tahiri.gestiondestock.repository.FournisseurRepository;
import com.tahiri.gestiondestock.validator.FournisseurValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class FournisseurService {

    @Autowired
    private FournisseurRepository fournisseurRepository;
    @Autowired
    private CommandeFournisseurRepository commandeFournisseurRepository;

    public Fournisseur save(Fournisseur fournisseur) {
        List<String> errors = FournisseurValidator.validate(fournisseur);
        if (!errors.isEmpty()) {
            log.error("Fournisseur is not valid {}", fournisseur);
            throw new InvalidEntityException("Le fournisseur n'est pas valide", ErrorCodes.FOURNISSEUR_NOT_VALID, errors);
        }

        return
        fournisseurRepository.save(fournisseur);
    }

    public Fournisseur getById(Integer id) {
        if (id == null) {
            log.error("Fournisseur ID is null");
            return null;
        }
        return fournisseurRepository.findById(id)

                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun fournisseur avec l'ID = " + id + " n' ete trouve dans la BDD",
                        ErrorCodes.FOURNISSEUR_NOT_FOUND)
                );
    }

    public List<Fournisseur> getdAll() {
        return fournisseurRepository.findAll();

    }
    public void delete(Integer id) {
        if (id == null) {
            log.error("Fournisseur ID is null");
            return;
        }
        List<CommandeFournisseur> commandeFournisseur = commandeFournisseurRepository.findByFournisseur_Id(id);
        if (!commandeFournisseur.isEmpty()) {
            throw new InvalidOperationException("Impossible de supprimer un fournisseur qui a deja des commandes",
                    ErrorCodes.FOURNISSEUR_ALREADY_IN_USE);
        }
        fournisseurRepository.deleteById(id);
    }


}
