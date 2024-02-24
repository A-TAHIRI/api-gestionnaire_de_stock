package com.tahiri.gestiondestock.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tahiri.gestiondestock.model.Adresse;

import com.tahiri.gestiondestock.model.Client;
import com.tahiri.gestiondestock.model.Photo;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data


public class ClientDto {

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
    private List<CommandeClientDto> commandeClients;

    public ClientDto (Client client) {
        this.id=client.getId();
        this.nom=client.getNom();
        this.prenom=client.getPrenom();
        this.adresse= new AdresseDto(client.getAdresse());
        this.photo= client.getPhoto();
        this.photos=client.getPhotos();




    }




}
