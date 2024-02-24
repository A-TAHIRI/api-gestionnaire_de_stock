package com.tahiri.gestiondestock.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;

import com.tahiri.gestiondestock.model.Fournisseur;
import com.tahiri.gestiondestock.model.Photo;
import lombok.Data;

import java.util.List;


@Data

public class FournisseurDto {
    private Integer id;
    private String nom;
    private String prenom;
    private AdresseDto adresse;
    private String photo;
    private String email;
    private String numTel;
    private Integer idEntreprise;
    private  List<Photo> photos;
    @JsonIgnore
    private List<CommandeFournisseurDto> commandeFournisseurs;

    public FournisseurDto (Fournisseur fournisseur) {
        this.id=fournisseur.getId();
        this.nom=fournisseur.getNom();
        this.prenom=fournisseur.getPrenom();
        this.adresse= new AdresseDto(fournisseur.getAdresse());
        this.photo= fournisseur.getPhoto();
        this.email=fournisseur.getEmail();
        this.numTel=fournisseur.getNumTel();
        this.idEntreprise=fournisseur.getIdEntreprise();
        this.photos=fournisseur.getPhotos();


    }


}
