package com.tahiri.gestiondestock.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Fournisseur")

public class Fournisseur extends AbstractEntity {
    @Column(name = "nom")
    private  String nom;

    @Column(name = "prenom")
    private  String prenom;

    @Embedded
    private  Adresse adresse;


    @Column(name = "photo")
    private  String photo;

    @Column(name = "mail")
    private  String email;

    @Column(name = "numTel")
    private  String numTel;

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @OneToMany(mappedBy = "fournisseur")
    private List<CommandeFournisseur> commandeFournisseurs;

    @OneToMany
    private List<Photo> photos;

    public boolean hasIdentreprise() {
        return idEntreprise != null ;
    }
}
