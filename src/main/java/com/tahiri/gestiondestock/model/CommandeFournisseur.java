package com.tahiri.gestiondestock.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CommandeFournisseur")

public class CommandeFournisseur extends AbstractEntity{

    @Column(name = "reference")
    private  String reference ;

    @Column(name = "dateCommande")
    private Instant dateCommande;

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "totalPrix")
    private Float totalPrix;

    @Column(name = "etatcommande")
    @Enumerated(EnumType.STRING)
    private EtatCommande etatCommande;


    @ManyToOne
    @JoinColumn(name="idfournisseur")
    private  Fournisseur fournisseur;

    @OneToMany(mappedBy = "commandeFournisseur")
    @NotNull(message = "La liste des lignes de commande ne peut pas être nulle")
    private List<LigneCommandeFournisseur> ligneCommandeFournisseurs = new ArrayList<LigneCommandeFournisseur>();

    public boolean isCommandeLivree() {
        return EtatCommande.LIVREE.equals(this.etatCommande);
    }





}
