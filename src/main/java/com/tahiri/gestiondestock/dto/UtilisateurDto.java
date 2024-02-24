package com.tahiri.gestiondestock.dto;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tahiri.gestiondestock.model.Utilisateur;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@Data
public class UtilisateurDto {
    private  Integer id;
    private  String nom;
    private  String prenom;
    private AdresseDto adresse;
    private  String photo;
    private  String email;
    private  String numTel;
    private Date dateDeNaissance;
    private String mdp;
    private String token;
    private EntrepriseDto entreprise;
    private List<RoleDto> roles = new ArrayList<RoleDto>();

    public  UtilisateurDto (Utilisateur utilisateur) {
        this.id=utilisateur.getId();
        this.nom=utilisateur.getNom();
        this.prenom=utilisateur.getPrenom();
        this.adresse=new AdresseDto(utilisateur.getAdresse());
        this.photo=utilisateur.getPhoto();
        this.email=utilisateur.getEmail();
        this.numTel=utilisateur.getNumTel();
        this.dateDeNaissance=utilisateur.getDateDeNaissance();
        this.mdp=utilisateur.getMdp();
        this.token=utilisateur.getToken();
        utilisateur.getRoles().forEach( role ->
                this.roles.add(new RoleDto(role))
        );


        this.entreprise= new EntrepriseDto(utilisateur.getEntreprise());
        }



    }



