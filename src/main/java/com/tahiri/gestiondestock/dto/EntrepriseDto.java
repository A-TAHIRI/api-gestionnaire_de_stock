package com.tahiri.gestiondestock.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tahiri.gestiondestock.model.Entreprise;
import com.tahiri.gestiondestock.model.Photo;
import lombok.Data;


import java.util.List;


@Data
public class EntrepriseDto {

    private  Integer id;
    private  String nom;
    private String description;
    private AdresseDto adresse;
    private String codeFiscal;
    private String image;
    private String numTel;
    private String email;
    private String siteWeb;
    private  List<Photo> photos;
@JsonIgnore
    private List<UtilisateurDto> utilisateurs;

public  EntrepriseDto (Entreprise entreprise){
    this.id=entreprise.getId();
    this.nom=entreprise.getNom();
    this.description=entreprise.getDescription();
    this.adresse=new AdresseDto(entreprise.getAdresse());
    this.codeFiscal= entreprise.getCodeFiscal();
    this.image=entreprise.getImage();
    this.numTel=entreprise.getNumTel();
    this.email=entreprise.getEmail();
    this.siteWeb=entreprise.getSiteWeb();
    this.photos=entreprise.getPhotos();

    }



}
