package com.tahiri.gestiondestock.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Article")

public class Article extends  AbstractEntity{

    @Column(name = "codeArticle")
    private  String codeArticle;

    @Column(name="designation")
    private String designation;

    @Column(name = "prixUnitaireHt")
    private Float prixUnitaireHt;

    @Column(name = "tauxTva")
    private  Float tauxTva;

    @Column(name = "prixUnitaireTtc")
    private  Float prixUnitaireTtc;

    @Column(name = "image")
    private  String image;

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @ManyToOne
    @JoinColumn(name = "idcategorie")
    private  Categorie categorie;

    @OneToMany(mappedBy = "article")
    private List<LigneCommadeClient>  ligneCommadeClients;

    @OneToMany(mappedBy = "article")
    private List<LigneCommandeFournisseur> ligneCommandeFournisseurs;


    @OneToMany(mappedBy = "article")
    private List<LigneVente> ligneVentes;

    @OneToMany(mappedBy = "article")
    private List<MvtStk> mvtStks;

    @OneToMany
    private List<Photo> photos;







}
