package com.tahiri.gestiondestock.service;

import com.tahiri.gestiondestock.exception.EntityNotFoundException;
import com.tahiri.gestiondestock.exception.ErrorCodes;
import com.tahiri.gestiondestock.exception.InvalidEntityException;
import com.tahiri.gestiondestock.manager.TokenManager;
import com.tahiri.gestiondestock.model.Entreprise;
import com.tahiri.gestiondestock.model.Role;
import com.tahiri.gestiondestock.model.Utilisateur;
import com.tahiri.gestiondestock.repository.EntrepriseRepository;
import com.tahiri.gestiondestock.repository.RolesRepository;
import com.tahiri.gestiondestock.validator.EntrepriseValidator;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


@Service
@Slf4j
public class EntrepriseService {
    @Autowired
    private ApplicationUserDetailService applicationUserDetailService;

    @Autowired
    private EntrepriseRepository entrepriseRepository;
    @Autowired
    private UtilisateurService utilisateurService;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private RoleService roleService;


    public Entreprise save(Entreprise entreprise) {
        List<String> errors = EntrepriseValidator.validate(entreprise);
        if (!errors.isEmpty()) {
            log.error("Entreprise is not valid {}", entreprise);
            throw new InvalidEntityException("L'entreprise n'est pas valide", ErrorCodes.ENTREPRISE_NOT_VALID, errors);
        }

        Entreprise savedEntreprise=   entrepriseRepository.save(entreprise);
        Utilisateur utilisateur = fromEntreprise(savedEntreprise);
        Utilisateur savedUser = utilisateurService.save(utilisateur);
        savedEntreprise.setUtilisateurs(new ArrayList<>());
        savedEntreprise.getUtilisateurs().add(savedUser); // lien entre entreprise et utilisateur
        savedEntreprise = this.entrepriseRepository.save(entreprise); // sauvegarder



        return  savedEntreprise;

    }

    private Utilisateur fromEntreprise(Entreprise entreprise) {

          Utilisateur utilisateur = new Utilisateur();

          utilisateur.setAdresse(entreprise.getAdresse());
          utilisateur.setEmail(entreprise.getEmail());
          utilisateur.setNom(entreprise.getNom());
          utilisateur.setPrenom(entreprise.getCodeFiscal());
          utilisateur.setDateDeNaissance(new Date());
          utilisateur.setMdp(generateRandomPassword());
          utilisateur.setNumTel(entreprise.getNumTel());
          utilisateur.setRoles(List.of(roleService.addRole("ADMIN")));
          utilisateur.setActive(true);
          utilisateur.setToken(TokenManager.generateToken(applicationUserDetailService));
          utilisateur.setPhoto(entreprise.getImage());
          utilisateur.setEntreprise(entreprise);

     return  utilisateur;
    }
    private String generateRandomPassword() {
        return ("som3R@nd0mP@$$word");
    }

    public Entreprise getById(Integer id) {
        if (id == null) {
            log.error("Entreprise ID is null");
            return null;
        }
        return entrepriseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune entreprise avec l'ID = " + id + " n' ete trouve dans la BDD",
                        ErrorCodes.ENTREPRISE_NOT_FOUND)
                );
    }

    public List<Entreprise> getAll() {
        return entrepriseRepository.findAll();
    }
    public void delete(Integer id) {
        if (id == null) {
            log.error("Entreprise ID is null");
            return;
        }
        entrepriseRepository.deleteById(id);
    }

    private static String random(String chaine, int len) {
        StringBuilder sb = new StringBuilder(len);
        Random rnd = new Random();
        for (int i = 0; i < len; i++)
            sb.append(chaine.charAt(rnd.nextInt(chaine.length())));
        return sb.toString();
    }
}
