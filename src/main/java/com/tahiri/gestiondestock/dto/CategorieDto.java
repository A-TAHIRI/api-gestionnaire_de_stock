package com.tahiri.gestiondestock.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tahiri.gestiondestock.model.Categorie;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data

public class CategorieDto {
    private Integer id;
    private String code;
    private String designation;
    private  String image;
    private Integer idEntreprise;

    @JsonIgnore
    private List<ArticleDto> articles;


    public CategorieDto (Categorie categorie) {
        this.id=categorie.getId();
        this.code=categorie.getCode();
        this.designation=categorie.getDesignation();
        this.image=categorie.getImage();
        this.idEntreprise=categorie.getIdEntreprise();

    }

}
