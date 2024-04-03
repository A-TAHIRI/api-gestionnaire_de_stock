package com.tahiri.gestiondestock.validator;

import com.tahiri.gestiondestock.model.LigneCommandeClient;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class LigneCommadeClientValidator {
    public static  List<String> validate(LigneCommandeClient ligneCommadeClient){
        List<String> errors = new ArrayList<>();
        if (ligneCommadeClient == null) {
            errors.add("Veuillez renseigner les données de l'article et la quantité");
            return errors;
        }

        if (!StringUtils.hasLength(ligneCommadeClient.getArticle().getCodeArticle())) {
            errors.add("Veuillez renseigner le code de l'article");
        }
        if ((ligneCommadeClient.getQuantite() ==null)) {
            errors.add("Veuillez renseigner la qantité");
        }
        return  errors;
    }

}
