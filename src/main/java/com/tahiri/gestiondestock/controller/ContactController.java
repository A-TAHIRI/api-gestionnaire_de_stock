package com.tahiri.gestiondestock.controller;


import com.tahiri.gestiondestock.dto.ArticleDto;
import com.tahiri.gestiondestock.dto.ContactDto;
import com.tahiri.gestiondestock.model.Article;
import com.tahiri.gestiondestock.model.Contact;
import com.tahiri.gestiondestock.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.tahiri.gestiondestock.utils.constant.ARTICLE_ENDPOINT;

@RestController
@RequestMapping("/contact")
public class ContactController {


    @Autowired
    private ContactService contactService;

    /**
     * method pour ajouter un contact
     * @param contact
     * @return
     */
    @PostMapping("")
    public ContactDto enregister(@RequestBody Contact contact ){
        return new ContactDto(contactService.save(contact));
    }


}
