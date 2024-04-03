package com.tahiri.gestiondestock.dto;


import com.tahiri.gestiondestock.model.LigneCommandeClient;

import lombok.Data;

import java.math.BigDecimal;


@Data

public class LigneCommadeClientDto {
    private Integer id;
    private BigDecimal quantite;
    private double prixUnitaire;
    private Integer idEntreprise;
    private ArticleDto article;

    private CommandeClientDto commandeClient;

    public LigneCommadeClientDto (LigneCommandeClient ligneCommadeClient) {
        this.id=ligneCommadeClient.getId();
        this.quantite=ligneCommadeClient.getQuantite();
        this.prixUnitaire=ligneCommadeClient.getPrixUnitaire();
        this.idEntreprise=ligneCommadeClient.getIdEntreprise();
        this.article= new ArticleDto(ligneCommadeClient.getArticle());
        this.commandeClient=new CommandeClientDto(ligneCommadeClient.getCommandeClient());


    }




}
