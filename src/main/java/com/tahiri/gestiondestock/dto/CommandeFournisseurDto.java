package com.tahiri.gestiondestock.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tahiri.gestiondestock.model.CommandeFournisseur;
import com.tahiri.gestiondestock.model.EtatCommande;
import com.tahiri.gestiondestock.model.LigneCommandeFournisseur;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Data


public class CommandeFournisseurDto {

    private Integer id;
    private String reference;


    private Instant dateCommande;
    private Integer idEntreprise;
    private Float totalPrix;
    private EtatCommande etatCommande;
    private FournisseurDto fournisseur;


    private List<Integer> ligneCommandeFournisseurs ;

    public CommandeFournisseurDto (CommandeFournisseur commandeFournisseur) {
           this.id=commandeFournisseur.getId();
           this.reference=commandeFournisseur.getReference();
           this.dateCommande=commandeFournisseur.getDateCommande();
           this.totalPrix=commandeFournisseur.getTotalPrix();
           this.etatCommande=commandeFournisseur.getEtatCommande();
           this.idEntreprise=commandeFournisseur.getIdEntreprise();
           this.fournisseur= new FournisseurDto(commandeFournisseur.getFournisseur());
           List<Integer> ling_id=new ArrayList<>();
           if (commandeFournisseur.getLigneCommandeFournisseurs() != null){
               for (LigneCommandeFournisseur lig : commandeFournisseur.getLigneCommandeFournisseurs()){
                   ling_id.add(lig.getId());
               }
               this.ligneCommandeFournisseurs=ling_id;
           }




        }





}
