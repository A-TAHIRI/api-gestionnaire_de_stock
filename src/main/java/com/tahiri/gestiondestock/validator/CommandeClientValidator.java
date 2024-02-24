package com.tahiri.gestiondestock.validator;



import com.tahiri.gestiondestock.model.CommandeClient;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CommandeClientValidator {
    public static  List<String> validate(CommandeClient commandeClient){
        List<String> errors = new ArrayList<>();
        if (commandeClient == null) {
            errors.add("Veuillez renseigner le code de la commande");
            errors.add("Veuillez renseigner la date de la commande");
            errors.add("Veuillez renseigner l'etat de la commande");
            errors.add("Veuillez renseigner le client");
            return errors;
        }

        if (!StringUtils.hasLength(commandeClient.getReference())) {
            errors.add("Veuillez renseigner le code de la commande");
        }
        if (commandeClient.getDateCommande() == null) {
            errors.add("Veuillez renseigner la date de la commande");
        }
        if (!StringUtils.hasLength(commandeClient.getEtatCommande().toString())) {
            errors.add("Veuillez renseigner l'etat de la commande");
        }
        if (commandeClient.getClient() == null || commandeClient.getClient().getId() == null) {
            errors.add("Veuillez renseigner le client");
        }

        return errors;
    }
    }


