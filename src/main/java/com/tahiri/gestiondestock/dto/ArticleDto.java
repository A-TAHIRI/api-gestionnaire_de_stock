package com.tahiri.gestiondestock.dto;



import com.tahiri.gestiondestock.model.Article;
import com.tahiri.gestiondestock.model.Photo;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data

public class ArticleDto {
    private Integer id;
    private String codeArticle;
    private String designation;
    private Float prixUnitaireHt;
    private Float tauxTva;
    private Float prixUnitaireTtc;
    private String image;
    private CategorieDto categorie;
    private Integer idEntreprise;
    private List<Photo> photos;

    public  ArticleDto (Article article) {

        this.id=article.getId();
        this.codeArticle=article.getCodeArticle();
        this.designation=article.getDesignation();
        this.prixUnitaireHt=article.getPrixUnitaireHt();
        this.tauxTva=article.getTauxTva();
        this.prixUnitaireTtc=article.getPrixUnitaireTtc();
        this.image=article.getImage();
        this.categorie=new CategorieDto(article.getCategorie());
        this.idEntreprise=article.getIdEntreprise();
        this.photos=article.getPhotos();


    }



}
