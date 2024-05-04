package com.tahiri.gestiondestock.dto;



import com.tahiri.gestiondestock.model.LigneCommandeFournisseur;
import com.tahiri.gestiondestock.model.MvtStk;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Data

public class LigneCommadeFournisseurDto {

    private Integer id;
    private BigDecimal quantite;

    private double prixUnitaire;
    private Integer idEntreprise;

    private ArticleDto article;

    private CommandeFournisseurDto commandeFournisseur;

    public LigneCommadeFournisseurDto (LigneCommandeFournisseur ligneCommandeFournisseur) {
        this.id=ligneCommandeFournisseur.getId();
        this.quantite=ligneCommandeFournisseur.getQuantite();
        this.prixUnitaire=ligneCommandeFournisseur.getPrixUnitaire();
        this.idEntreprise=ligneCommandeFournisseur.getIdEntreprise();
        this.article= new ArticleDto(ligneCommandeFournisseur.getArticle());
        this.commandeFournisseur= new CommandeFournisseurDto(ligneCommandeFournisseur.getCommandeFournisseur());


        }

}
