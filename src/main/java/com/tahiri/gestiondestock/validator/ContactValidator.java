package com.tahiri.gestiondestock.validator;

import com.tahiri.gestiondestock.model.Contact;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ContactValidator {

    public static List<String> validate(Contact contact){
        List<String> errors = new ArrayList<>();
        if (contact == null){
            errors.add("Veuillez renseigner l'email");
            errors.add("Veuillez renseigner le sujet");
            errors.add("Veuillez renseigner le message");
        }
        if (!StringUtils.hasLength(contact.getEmail())) {
            errors.add("Veuillez renseigner l'email");
        }
        if (!StringUtils.hasLength(contact.getSujet())) {
            errors.add("Veuillez renseigner le sujet");
        }
        if (!StringUtils.hasLength(contact.getMessage())) {
            errors.add("Veuillez renseigner le message");
        }

        return errors;
    }

}
