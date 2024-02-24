package com.tahiri.gestiondestock.dto;



import com.tahiri.gestiondestock.model.MvtStk;
import com.tahiri.gestiondestock.model.SourceMvtStk;
import com.tahiri.gestiondestock.model.TypeMvtStk;


import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;


@Data


public class MvtStkDto {
    private Integer id;
    private Instant dateMvt;
    private BigDecimal quantite;
    private TypeMvtStk typeMvt;
    private Integer idEntreprise;
    private ArticleDto article;
    private SourceMvtStk sourceMvt;

    public MvtStkDto (MvtStk mvtStk) {
        this.id=mvtStk.getId();
        this.dateMvt=mvtStk.getDateMvt();
        this.quantite=mvtStk.getQuantite();
        this.typeMvt=mvtStk.getTypeMvt();
        this.idEntreprise=mvtStk.getIdEntreprise();
        this.article=new ArticleDto(mvtStk.getArticle());
        this.sourceMvt=mvtStk.getSourceMvt();


    }



}
