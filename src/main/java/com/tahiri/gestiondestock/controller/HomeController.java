package com.tahiri.gestiondestock.controller;


import com.tahiri.gestiondestock.model.Utilisateur;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.MDC;

import org.springframework.web.bind.annotation.RestController;

import static com.tahiri.gestiondestock.utils.constant.ARTICLE_ENDPOINT;

@RestController

@CrossOrigin(origins = {"http://localhost:4200","https://monsite.fr","http://localhost:56678"})
public class HomeController {

    @GetMapping("")
  public void   getIdEntreprise(@AuthenticationPrincipal Utilisateur utilisateur){
     String idEntreprise = utilisateur.getEntreprise().getId().toString();
     System.out.println( "IdEntreprise est :"+ idEntreprise);

     MDC.put("idEntreprise",idEntreprise);

    }
}
