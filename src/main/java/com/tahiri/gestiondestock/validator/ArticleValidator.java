package com.tahiri.gestiondestock.validator;



import com.tahiri.gestiondestock.model.Article;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArticleValidator {
    public static  List<String> validate(Article article){
        List<String> errors = new ArrayList<>();
        if (article == null) {
            errors.add("Veuillez renseigner le code de l'article");
            errors.add("Veuillez renseigner la designation de l'article");
            errors.add("Veuillez renseigner le prix unitaire HT l'article");
            errors.add("Veuillez renseigner le taux TVA de l'article");
            errors.add("Veuillez renseigner le prix unitaire TTC de l'article");
            errors.add("Veuillez selectionner une categorie");
            return errors;
        }

        if (!StringUtils.hasLength(article.getCodeArticle())) {
            errors.add("Veuillez renseigner le code de l'article");
        }
        if (!StringUtils.hasLength(article.getDesignation())) {
            errors.add("Veuillez renseigner la designation de l'article");
        }
        if (Objects.equals(article.getPrixUnitaireHt(), null)) {
            errors.add("Veuillez renseigner le prix unitaire HT de l'article");
        }

        if (Objects.equals(article.getTauxTva(),null)) {
            errors.add("Veuillez renseigner le taux TVA de l'article");
        }
        if (Objects.equals(article.getPrixUnitaireTtc() , null)) {
            errors.add("Veuillez renseigner le prix unitaire TTC de l'article");
        }
        if (article.getCategorie() == null || article.getCategorie().getId() == null) {
            errors.add("Veuillez selectionner une categorie");
        }
        return errors;
    }

}


