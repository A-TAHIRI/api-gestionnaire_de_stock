package com.tahiri.gestiondestock.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "LigneCommandeFournisseur")

public class LigneCommandeFournisseur extends  AbstractEntity {

    @Column(name = "quantite")
    private BigDecimal quantite;

    @Column(name = "prixUnitaire")
    private double prixUnitaire;

    @Column(name = "identreprise")
    private Integer idEntreprise;


    @ManyToOne
    @JoinColumn(name = "idarticle")
    private  Article article;

    @ManyToOne
    @JoinColumn(name = "idcommandeFournisseur")
    private  CommandeFournisseur commandeFournisseur;

    public boolean hasIdentreprise() {
        return idEntreprise != null ;
    }

}
