package com.tahiri.gestiondestock.controller;


import com.tahiri.gestiondestock.dto.ArticleDto;
import com.tahiri.gestiondestock.dto.ClientDto;
import com.tahiri.gestiondestock.dto.EntrepriseDto;
import com.tahiri.gestiondestock.model.Article;
import com.tahiri.gestiondestock.model.Client;
import com.tahiri.gestiondestock.model.Entreprise;
import com.tahiri.gestiondestock.service.EntrepriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.tahiri.gestiondestock.utils.constant.ENTREPRISE_ENDPOINT;

@RestController
@RequestMapping(ENTREPRISE_ENDPOINT)
public class EntrepriseController {
    @Autowired
    private EntrepriseService entrepriseService;


    /**
     * method pour récupirer tous les entreprises
     * @return une liste des entreprises
     */

    @GetMapping("")
    public List<EntrepriseDto> tousEntreprises(){
        List<Entreprise> entreprises = entrepriseService.getAll();
        List<EntrepriseDto> entrepriseDtos = new ArrayList<>();
        for (Entreprise E: entreprises){
            entrepriseDtos.add(new EntrepriseDto(E));
        }
        return entrepriseDtos;
    }

    /**
     * method pour recupirer une entreprise
     * @param id
     * @return une entreprise de l'id
     */
    @GetMapping("/{id}")
    public  EntrepriseDto uneEntreprise(@PathVariable Integer id){
        return  new EntrepriseDto(entrepriseService.getById(id));

    }

    /**
     * method pour ajouter une entreprise
     * @return entreprise
     */
    @PostMapping("")
    public  EntrepriseDto enregister(@RequestBody Entreprise entreprise ){
        return new EntrepriseDto(entrepriseService.save(entreprise));
    }

    /**
     * supprimer l'entreprise par id
     * @param id
     */

    @DeleteMapping("/{id}")
    public void  supprimer( @PathVariable Integer id){
        entrepriseService.delete(id);

    }

    /**
     * modifier le une entreprise
     * @param id
     * @param entreprise
     * @return
     */
    @PutMapping("/{id}")
    public EntrepriseDto modifier(@PathVariable Integer id , @RequestBody Entreprise  entreprise ){
        Entreprise old = entrepriseService.getById(id);
        if (old != null) {
            entreprise.setId(id);

            return  new EntrepriseDto(entrepriseService.save(entreprise));
        }
        return  new EntrepriseDto(entrepriseService.save(entreprise));
    }


}
