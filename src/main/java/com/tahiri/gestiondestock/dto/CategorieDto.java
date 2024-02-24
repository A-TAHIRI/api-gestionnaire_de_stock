package com.tahiri.gestiondestock.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tahiri.gestiondestock.model.Categorie;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data

public class CategorieDto {
    private Integer id;
    private String code;
    private String designation;
    private Integer idEntreprise;

    @JsonIgnore
    private List<ArticleDto> articles;


    public CategorieDto (Categorie categorie) {
        this.id=categorie.getId();
        this.code=categorie.getCode();
        this.designation=categorie.getDesignation();
        this.idEntreprise=categorie.getIdEntreprise();

    }

}
