package com.tahiri.gestiondestock.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tahiri.gestiondestock.model.CommandeFournisseur;
import com.tahiri.gestiondestock.model.EtatCommande;
import lombok.Data;

import java.time.Instant;
import java.util.List;


@Data


public class CommandeFournisseurDto {

    private Integer id;
    private String reference;


    private Instant dateCommande;
    private Integer idEntreprise;
    private EtatCommande etatCommande;
    private FournisseurDto fournisseur;

    @JsonIgnore
    private List<LigneCommadeFournisseurDto> ligneCommandeFournisseurs;

    public CommandeFournisseurDto (CommandeFournisseur commandeFournisseur) {
           this.id=commandeFournisseur.getId();
           this.reference=commandeFournisseur.getReference();
           this.dateCommande=commandeFournisseur.getDateCommande();
           this.etatCommande=commandeFournisseur.getEtatCommande();
           this.idEntreprise=commandeFournisseur.getIdEntreprise();
           this.fournisseur= new FournisseurDto(commandeFournisseur.getFournisseur());

        }





}
