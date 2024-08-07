package com.tahiri.gestiondestock.dto;


import com.tahiri.gestiondestock.model.Adresse;
import com.tahiri.gestiondestock.model.Contact;
import lombok.Data;

@Data
public class ContactDto {
    private  String nom;
    private  String email;
    private  String sujet;
    private  String message;
    public  ContactDto (Contact contact) {
        this.nom=contact.getNom();
        this.email=contact.getEmail();
        this.sujet=contact.getSujet();
        this.message= contact.getMessage();


    }

}
