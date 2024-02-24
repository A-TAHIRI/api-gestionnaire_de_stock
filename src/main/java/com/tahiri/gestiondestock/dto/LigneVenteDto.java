package com.tahiri.gestiondestock.dto;


import com.tahiri.gestiondestock.model.LigneVente;
import lombok.Data;

import java.math.BigDecimal;


@Data


public class LigneVenteDto {
    private Integer id;
    private BigDecimal quantite;
    private double prixUnitaire;
    private Integer idEntreprise;

    private VenteDto vente;

    private ArticleDto article;

    public LigneVenteDto (LigneVente ligneVente) {

        this.id=ligneVente.getId();
        this.quantite=ligneVente.getQuantite();
        this.prixUnitaire=ligneVente.getPrixUnitaire();
        this.idEntreprise=ligneVente.getIdEntreprise();
        this.vente= new VenteDto(ligneVente.getVente());
        this.article=new ArticleDto(ligneVente.getArticle());
    }


}
