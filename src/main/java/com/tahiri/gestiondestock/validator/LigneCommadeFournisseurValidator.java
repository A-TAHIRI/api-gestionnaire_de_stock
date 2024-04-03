package com.tahiri.gestiondestock.validator;

import com.tahiri.gestiondestock.model.LigneCommandeFournisseur;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class LigneCommadeFournisseurValidator {
    public static  List<String> validate(LigneCommandeFournisseur ligneCommandeFournisseur){
        List<String> errors = new ArrayList<>();
        if (ligneCommandeFournisseur == null) {
            errors.add("Veuillez renseigner les données de l'article et la quantité");
            return errors;
        }

        if (!StringUtils.hasLength(ligneCommandeFournisseur.getArticle().getCodeArticle())) {
            errors.add("Veuillez renseigner le code de l'article");
        }
        if ((ligneCommandeFournisseur.getQuantite() ==null)) {
            errors.add("Veuillez renseigner le code de l'article");
        }
        return  errors;
    }
    }


