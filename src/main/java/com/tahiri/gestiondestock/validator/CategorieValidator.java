package com.tahiri.gestiondestock.validator;

import com.tahiri.gestiondestock.model.Categorie;
import org.springframework.util.StringUtils;


import java.util.ArrayList;
import java.util.List;

public class CategorieValidator {
    public static  List<String> validate(Categorie categorie){
        List<String> errors = new ArrayList<>();

        if (categorie == null || !StringUtils.hasLength(categorie.getCode())) {
            errors.add("Veuillez renseigner le code de la categorie");
        }
        return  errors;
    }

}
