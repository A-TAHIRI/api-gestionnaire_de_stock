package com.tahiri.gestiondestock.validator;


import com.tahiri.gestiondestock.model.Vente;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class VenteValidator {
    public static  List<String> validate(Vente vente){
        List<String> errors = new ArrayList<>();
        if (vente == null) {
            errors.add("Veuillez renseigner le code de la commande");
            errors.add("Veuillez renseigner la date de la commande");
            return errors;
        }

        if (!StringUtils.hasLength(vente.getReference())) {
            errors.add("Veuillez renseigner le code de la commande");
        }
        if (vente.getDateVente() == null) {
            errors.add("Veuillez renseigner la date de la commande");
        }

        return errors;
    }


}
