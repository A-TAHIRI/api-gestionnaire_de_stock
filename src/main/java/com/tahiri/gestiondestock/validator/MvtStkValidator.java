package com.tahiri.gestiondestock.validator;


import com.tahiri.gestiondestock.model.MvtStk;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MvtStkValidator {
    public static  List<String> validate(MvtStk mvtStk){
        List<String> errors = new ArrayList<>();
        if (mvtStk == null) {
            errors.add("Veuillez renseigner la date du mouvenent");
            errors.add("Veuillez renseigner la quantite du mouvenent");
            errors.add("Veuillez renseigner l'article");
            errors.add("Veuillez renseigner le type du mouvement");

            return errors;
        }
        if (mvtStk.getDateMvt() == null) {
            errors.add("Veuillez renseigner la date du mouvenent");
        }
        if (mvtStk.getQuantite() == null || mvtStk.getQuantite().compareTo(BigDecimal.ZERO) == 0) {
            errors.add("Veuillez renseigner la quantite du mouvenent");
        }
        if (mvtStk.getArticle() == null || mvtStk.getArticle().getId() == null) {
            errors.add("Veuillez renseigner l'article");
        }
        if (!StringUtils.hasLength(mvtStk.getTypeMvt().name())) {
            errors.add("Veuillez renseigner le type du mouvement");
        }

        return errors;
    }


}
