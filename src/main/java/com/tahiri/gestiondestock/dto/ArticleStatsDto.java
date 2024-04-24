package com.tahiri.gestiondestock.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tahiri.gestiondestock.model.Article;

import com.tahiri.gestiondestock.model.ArticleStats;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleStatsDto {
    @JsonIgnore
    private Article article ;
    private Float  nbLigne ;
    private Float snumQuantite ;
    private Float total;


}
