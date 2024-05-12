package com.tahiri.gestiondestock.service;


import com.tahiri.gestiondestock.exception.EntityNotFoundException;
import com.tahiri.gestiondestock.exception.ErrorCodes;
import com.tahiri.gestiondestock.exception.InvalidEntityException;
import com.tahiri.gestiondestock.exception.InvalidOperationException;
import com.tahiri.gestiondestock.model.CommandeFournisseur;
import com.tahiri.gestiondestock.model.Fournisseur;
import com.tahiri.gestiondestock.model.Utilisateur;
import com.tahiri.gestiondestock.repository.CommandeFournisseurRepository;
import com.tahiri.gestiondestock.repository.FournisseurRepository;
import com.tahiri.gestiondestock.validator.FournisseurValidator;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.data.domain.PageRequest.of;


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

    /**
     * Service pour recupirer les fournisseur par page et par recherche
     * @param nom
     * @param page
     * @param size
     * @return
     */
    public Page<Fournisseur> getfournisseur(String nom, int page, int size){
        String identreprise = MDC.get("idEntreprise");
        return fournisseurRepository.findByNomContainingAndIdEntreprise(nom,Integer.valueOf(identreprise),of(page,size));
    }


    /**
     * Service qui retourne le nombre des fournisseur de  mois prisident
     * @return
     */

    public int countFournisseurBymouth(){
        String identreprise = MDC.get("idEntreprise");
        return this.fournisseurRepository.countFournisseursByMonthAndYear(identreprise);
    }

    /**
     * Service qui retourne le nombre des fournisseur de  mois actuel
     * @return
     */

    public int countFournisseuByThisMouth(){
        String identreprise = MDC.get("idEntreprise");
        return this.fournisseurRepository.countFournisseursByThisMonthAndYear(identreprise);
    }


    /**
     * Service qui retourne le nombre des fournisseur de cette année
     * @return
     */
    public int countFournisseuByYear(){
        String identreprise = MDC.get("idEntreprise");
        return this.fournisseurRepository.countFournisseursByYear(identreprise);
    }

    /**
     * Service qui retourne le nombre des fournisseur de l' année président
     * @return
     */
    public int countFournisseuByLastYear(){
        String identreprise = MDC.get("idEntreprise");
        return this.fournisseurRepository.countFournisseursByLastYear(identreprise);
    }



    /**
     * Service qui retourne le nombre des fournisseur de aujourd'huit
     * @return
     */
    public int countFournisseuByDay(){
        String identreprise = MDC.get("idEntreprise");
        return this.fournisseurRepository.countFournisseursByDay(identreprise);
    }
    /**
     * Service qui retourne le nombre des fournisseur d'hier
     * @return
     */
    public int countFournisseuByLastDay(){
        String identreprise = MDC.get("idEntreprise");
        LocalDate yesterdayDate = LocalDate.now().minusDays(1);
        Timestamp yesterdayTimestamp = Timestamp.valueOf(yesterdayDate.atStartOfDay());
        return this.fournisseurRepository.countFournisseursByYesterday(yesterdayTimestamp,identreprise);
    }


}
