package com.tahiri.gestiondestock.service;

import com.tahiri.gestiondestock.dto.ChangerMotDePasseUtilisateurDto;

import com.tahiri.gestiondestock.exception.EntityNotFoundException;
import com.tahiri.gestiondestock.exception.ErrorCodes;
import com.tahiri.gestiondestock.exception.InvalidEntityException;
import com.tahiri.gestiondestock.exception.InvalidOperationException;
import com.tahiri.gestiondestock.manager.TokenManager;
import com.tahiri.gestiondestock.model.Utilisateur;
import com.tahiri.gestiondestock.repository.UtilisateurRepository;
import com.tahiri.gestiondestock.validator.UtilisateurValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.springframework.data.domain.PageRequest.of;


@Service
@Slf4j
public class UtilisateurService{
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;


    @Autowired
    private  RoleService roleService;

    @Autowired
    private ApplicationUserDetailService applicationUserDetailService;


    String identreprise = MDC.get("idEntreprise");

    public Utilisateur save(Utilisateur utilisateur) {
        List<String> errors = UtilisateurValidator.validate(utilisateur);

        if (!errors.isEmpty()) {
            log.error("Utilisateur is not valid {}", utilisateur);
            throw new InvalidEntityException("L'utilisateur n'est pas valide", ErrorCodes.UTILISATEUR_NOT_VALID, errors);
        }
        if(!utilisateur.getEmail().contains("@")) {
            throw  new RuntimeException("Votre mail invalide");
        }

        if(utilisateur.getMdp().isEmpty() ||utilisateur.getMdp().length() < 6) {
            throw  new RuntimeException("Le mot de passe doit avoir au moins 6 caractères");
        }
         String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*\\d)(?!.*\\s).{6,}$";

        if (!utilisateur.getMdp().matches(PASSWORD_PATTERN)) {
            throw new RuntimeException("Le mot de passe doit contenir au moins une lettre majuscule, un caractère spécial, un chiffre et avoir au moins 6 caractères");
        }


        utilisateur.setMdp(passwordEncoder.encode(utilisateur.getMdp()));

        return
                utilisateurRepository.save(utilisateur);
    }

    public boolean userAlreadyExists(String email) {
        Optional<Utilisateur> user = utilisateurRepository.findByEmail(email);
        return user.isPresent();
    }

    public Utilisateur getById(Integer id) {
        if (id == null) {
            log.error("Utilisateur ID is null");
            return null;
        }
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun utilisateur avec l'ID = " + id + " n' ete trouve dans la BDD",
        ErrorCodes.UTILISATEUR_NOT_FOUND)
                );
    }

    public List<Utilisateur> getdAll() {
        return utilisateurRepository.findAll();

    }

    public void delete(Integer id) {
        if (id == null) {
            log.error("Utilisateur ID is null");
            return;
        }
        utilisateurRepository.deleteById(id);
    }
    public Utilisateur getByEmail(String email) {
        return utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(

                        "Aucun utilisateur avec l'email = " + email + " n' ete trouve dans la BDD",
                        ErrorCodes.UTILISATEUR_NOT_FOUND)
                );
    }

    public Utilisateur changerMotDePasse(ChangerMotDePasseUtilisateurDto dto) {
        validate(dto);
/*
        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(dto.getId());
        if (utilisateurOptional.isEmpty()) {
            log.warn("Aucun utilisateur n'a ete trouve avec l'ID " + dto.getId());
            throw new EntityNotFoundException("Aucun utilisateur n'a ete trouve avec l'ID " + dto.getId(), ErrorCodes.UTILISATEUR_NOT_FOUND);
        }
*/
        Utilisateur utilisateur = utilisateurRepository.findById(dto.getId()).orElseThrow(()->new EntityNotFoundException("Aucun utilasateur avec l'ID = " + dto.getId() + " n' ete trouve dans la BDD",
                ErrorCodes.UTILISATEUR_NOT_FOUND));
        utilisateur.setMdp(passwordEncoder.encode(dto.getMotDePasse()));

        return
                utilisateurRepository.save(utilisateur);

    }

    private void validate(ChangerMotDePasseUtilisateurDto dto) {
        if (dto == null) {
            log.warn("Impossible de modifier le mot de passe avec un objet NULL");
            throw new InvalidOperationException("Aucune information n'a ete fourni pour pouvoir changer le mot de passe",
                    ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID);
        }
        if (dto.getId() == null) {
            log.warn("Impossible de modifier le mot de passe avec un ID NULL");
            throw new InvalidOperationException("ID utilisateur null:: Impossible de modifier le mote de passe",
                    ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID);
        }
        if (!StringUtils.hasLength(dto.getMotDePasse()) || !StringUtils.hasLength(dto.getConfirmMotDePasse())) {
            log.warn("Impossible de modifier le mot de passe avec un mot de passe NULL");
            throw new InvalidOperationException("Mot de passe utilisateur null:: Impossible de modifier le mote de passe",
                    ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID);
        }
        if (!dto.getMotDePasse().equals(dto.getConfirmMotDePasse())) {
            log.warn("Impossible de modifier le mot de passe avec deux mots de passe different");
            throw new InvalidOperationException("Mots de passe utilisateur non conformes:: Impossible de modifier le mote de passe",
                    ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID);
        }
    }







    /**
     * Service pour recupirer les utilisateur par page et par recherche
     * @param nom
     * @param page
     * @param size
     * @return
     */
   public Page<Utilisateur> getUsers(String nom, int page, int size){
       String identreprise = MDC.get("idEntreprise");
        return utilisateurRepository.findByNomContainingAndEntrepriseId(nom, Integer.valueOf(identreprise),of(page,size));
   }

    /**
     * Service qui retourne le nombre des utilisateur de  mois prisident
     * @return
     */

   public int countUtilisateurBymouth(){
       String identreprise = MDC.get("idEntreprise");
       return this.utilisateurRepository.countUtilisateursByMonthAndYear(identreprise);
   }

    /**
     * Service qui retourne le nombre des utilisateur de  mois actuel
     * @return
     */

    public int countUtilisateurByThisMouth(){
        String identreprise = MDC.get("idEntreprise");
        return this.utilisateurRepository.countUtilisateursByThisMonthAndYear(identreprise);
    }


    /**
     * Service qui retourne le nombre des utilisateur de cette année
     * @return
     */
   public int countUtilisateurByYear(){
       String identreprise = MDC.get("idEntreprise");
      return this.utilisateurRepository.countUtilisateursByYear(identreprise);
   }

    /**
     * Service qui retourne le nombre des utilisateur de l' année président
     * @return
     */
    public int countUtilisateurByLastYear(){
        String identreprise = MDC.get("idEntreprise");
        return this.utilisateurRepository.countUtilisateursByLastYear(identreprise);
    }



    /**
     * Service qui retourne le nombre des utilisateur de aujourd'huit
     * @return
     */
    public int countUtilisateurByDay(){
        String identreprise = MDC.get("idEntreprise");
        return this.utilisateurRepository.countUtilisateursByDay(identreprise);
    }
    /**
     * Service qui retourne le nombre des utilisateur d'hier
     * @return
     */
    public int countUtilisateurByLastDay(){
        String identreprise = MDC.get("idEntreprise");
        LocalDate yesterdayDate = LocalDate.now().minusDays(1);
        Timestamp yesterdayTimestamp = Timestamp.valueOf(yesterdayDate.atStartOfDay());
        return this.utilisateurRepository.countUtilisateursByYesterday(yesterdayTimestamp,identreprise);
    }
}
