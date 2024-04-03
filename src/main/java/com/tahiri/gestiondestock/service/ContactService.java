package com.tahiri.gestiondestock.service;



import com.tahiri.gestiondestock.exception.ErrorCodes;
import com.tahiri.gestiondestock.exception.InvalidEntityException;
import com.tahiri.gestiondestock.model.Contact;
import com.tahiri.gestiondestock.repository.ContactRepository;
import com.tahiri.gestiondestock.validator.ContactValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ContactService {

    @Autowired
    private ContactRepository contactRepository ;

    /**
     * Service pour ajouter un Contact
     * @param contact
     * @return
     */
    public Contact save( Contact contact){
        List <String> errors = ContactValidator.validate(contact);
        if (!errors.isEmpty()){
            log.error("Contact non valid {}", contact);
            throw new InvalidEntityException("Contact n'est pas valide", ErrorCodes.CLIENT_NOT_VALID, errors);
        }
      return   this.contactRepository.save(contact);
    }

}
