package com.tahiri.gestiondestock.controller;



import com.tahiri.gestiondestock.dto.AuthRequestDto;
import com.tahiri.gestiondestock.dto.UtilisateurDto;
import com.tahiri.gestiondestock.exception.ErrorCodes;
import com.tahiri.gestiondestock.exception.InvalidEntityException;
import com.tahiri.gestiondestock.exception.WsException;
import com.tahiri.gestiondestock.manager.JwtsTokenGenerate;
import com.tahiri.gestiondestock.manager.TokenManager;
import com.tahiri.gestiondestock.model.Role;
import com.tahiri.gestiondestock.model.Utilisateur;
import com.tahiri.gestiondestock.service.ApplicationUserDetailService;
import com.tahiri.gestiondestock.service.EntrepriseService;
import com.tahiri.gestiondestock.service.RoleService;
import com.tahiri.gestiondestock.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


@RestController
@CrossOrigin(origins = {"http://localhost:4200","https://monsite.fr"})
public class AuthentificationController {

    @Autowired
    BCryptPasswordEncoder passwordEncoder;


@Autowired
private  RoleService roleService;

    @Autowired
    private ApplicationUserDetailService applicationUserDetailService;

    @Autowired
     private UtilisateurService utilisateurService;

    @Autowired
    private EntrepriseService entrepriseService;

    @PostMapping("/login")
    public Map<String, String>home(@RequestBody AuthRequestDto authRequestDto) {

        // associer le token à l'utilisateur
        // 1- vérifier le login
        Utilisateur userDetails = (Utilisateur) applicationUserDetailService.loadUserByUsername(authRequestDto.getEmail()); // il va me retourner l'utilisateur
        if (userDetails == null) { // l'utilisateur n'existe pas
            throw new WsException(HttpStatus.NOT_FOUND, "Le mot de passe ou ...");
        }

        // 2- Vérifier le mdp
        if (!passwordEncoder.matches(authRequestDto.getMdp(), userDetails.getMdp())){
            throw new WsException(HttpStatus.NOT_FOUND, "Le mot de passe ou ...");
        }
        // 3- générer le token
        /*String token;
        do {
            token = AleatoireManager.generateAZC(64);
        }while (userService.isTokenExiste(token)); // le token n'existe pas*/
        // il va sortir de la boucle lorsque le token n'existe pas
        // 4- Associer le token à l'utilisateur
        Utilisateur utilisateur = (Utilisateur) userDetails;
        // user.setToken(TokenManager.generateToken(userService)); // supprimer l'ancien pour générer un nouvel
        // userService.saveToken(user.getId(), user.getToken()); // modifier le token dans la base de donnée
        // 5- renvoyer le token
        Map<String, String> response = new HashMap<>();
        response.put("token",  JwtsTokenGenerate.generateToken(utilisateur.getToken()));

        return response;

    }

    /*
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody AuthRequestDto authRequestDto ){
        // vérifier email existe deja
        try{
            applicationUserDetailService.loadUserByUsername(authRequestDto.getEmail());
            throw new WsException(HttpStatus.BAD_REQUEST, "user already exist");
        }catch (Exception e){ }

        // l'ensemble des vérif => nom > 3 caract le mdp sup à 8 caract avec du majuscule ou minuscule

            Utilisateur newUser = new Utilisateur();
            newUser.setEmail(authRequestDto.getEmail());
            newUser.setMdp(authRequestDto.getMdp());
            newUser.setActive(true);
            newUser.setNom(authRequestDto.getNom());
            newUser.setPrenom(authRequestDto.getPrenom());
            newUser.setPhoto(authRequestDto.getPhoto());
            newUser.setDateDeNaissance(authRequestDto.getDateDeNaissance());
            newUser.setEntreprise(authRequestDto.getEntreprise());
            newUser.setAdresse(authRequestDto.getAdresse());
            newUser.setRoles( List.of(roleService.addRole("USER"))) ;
            newUser.setToken(TokenManager.generateToken(applicationUserDetailService));
            utilisateurService.save(newUser);

            Map<String, String> response = new HashMap<>();
            response.put("token",  JwtsTokenGenerate.generateToken(newUser.getToken()));

            return response;

    }
*/

    @PostMapping("/register")
    public UtilisateurDto register(@RequestBody Utilisateur utilisateur ){
        // vérifier email existe deja
        if (utilisateur.getId()==null){
            if(utilisateurService.userAlreadyExists(utilisateur.getEmail()) ) {
                throw new InvalidEntityException("Un autre utilisateur avec le meme email existe deja", ErrorCodes.UTILISATEUR_ALREADY_EXISTS,
                        Collections.singletonList("Un autre utilisateur avec le meme email existe deja dans la BDD"));
            }

        }

        // l'ensemble des vérif => nom > 3 caract le mdp sup à 8 caract avec du majuscule ou minuscule

        utilisateur.setRoles( List.of(roleService.addRole("USER"))) ;
        utilisateur.setToken(TokenManager.generateToken(applicationUserDetailService));
        utilisateurService.save(utilisateur);
        return new UtilisateurDto(utilisateur);

    }



}
