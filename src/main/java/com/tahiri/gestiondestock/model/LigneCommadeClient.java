package com.tahiri.gestiondestock.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Entity

@Data

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "LigneCommadeClient")

public class LigneCommadeClient extends AbstractEntity {

    @Column(name = "quantite")
    private BigDecimal quantite;


    @Column(name = "prixUnitaire")
    private double prixUnitaire;

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @ManyToOne
    @JoinColumn(name = "idarticle")
    private Article article;

    @ManyToOne
    @JoinColumn(name = "idcommandeClient")
    private CommandeClient commandeClient;
}
