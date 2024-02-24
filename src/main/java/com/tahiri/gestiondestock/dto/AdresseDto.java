package com.tahiri.gestiondestock.dto;


import com.tahiri.gestiondestock.model.Adresse;
import lombok.Data;

@Data
public class AdresseDto {

    private String adresse1;
    private String adresse2;
    private String ville;
    private String codePostale;
    private String pays;

    public  AdresseDto (Adresse adresse) {
        this.adresse1=adresse.getAdresse1();
        this.adresse2=adresse.getAdresse2();
        this.ville=adresse.getVille();
        this.codePostale=adresse.getCodePostale();
        this.pays=adresse.getPays();

    }





}
