package com.tahiri.gestiondestock.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Data

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CommandeClient")

public class CommandeClient extends  AbstractEntity {

    @Column(name = "reference")
    private  String reference;

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
    @JoinColumn(name = "idclient")
    private  Client client;

    @OneToMany(mappedBy = "commandeClient")
    @NotNull(message = "La liste des lignes de commande ne peut pas être nulle")
    private List<LigneCommandeClient> ligneCommandeClients;



    public boolean isCommandeLivree() {
        return EtatCommande.LIVREE.equals(this.etatCommande);
    }


}
