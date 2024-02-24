package com.tahiri.gestiondestock.controller;


import com.tahiri.gestiondestock.dto.CommandeClientDto;
import com.tahiri.gestiondestock.dto.CommandeFournisseurDto;
import com.tahiri.gestiondestock.model.CommandeClient;
import com.tahiri.gestiondestock.model.CommandeFournisseur;
import com.tahiri.gestiondestock.service.CommandeFournisseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.tahiri.gestiondestock.utils.constant.COMMADE_FOURNISSEUR_ENDPOINT;


@RestController
@RequestMapping(COMMADE_FOURNISSEUR_ENDPOINT)
@CrossOrigin(origins = {"http://localhost:4200","https://monsite.fr"})
public class CommandeFournisseurController {


    @Autowired

    private CommandeFournisseurService commandeFournisseurService;

    /**
     * recupirer tous les commades fournisseurs
     * @return
     */
    @GetMapping("")
    public List<CommandeFournisseurDto> tousLesCommades(){
        List<CommandeFournisseurDto> commandeFournisseurDtos = new ArrayList<>();
        List<CommandeFournisseur> commandeFournisseurs = commandeFournisseurService.getAll();
        for ( CommandeFournisseur c :commandeFournisseurs){
            commandeFournisseurDtos.add( new CommandeFournisseurDto(c));
        }
        return  commandeFournisseurDtos;
    }


    /**
     * pour recupirer une commade avec son id
     * @param id
     * @return
     */
    @GetMapping("/{id}")

    public  CommandeFournisseurDto uneCommadeFournisseur(@PathVariable Integer id ){
        return  new CommandeFournisseurDto(commandeFournisseurService.getById(id));

    }


    /**
     * ajouter une commade fournisseur  à la basses de données
     * @param commandeFournisseur
     * @return
     */
    @PostMapping("")
    public  CommandeFournisseurDto enregistrer( @RequestBody CommandeFournisseur commandeFournisseur){
        return  new CommandeFournisseurDto(commandeFournisseurService.save(commandeFournisseur));
    }

    /**
     * supprimer une commande fournisseur par son id
     * @param id
     */

    @DeleteMapping("/{id}")
    public  void  supprimer( @PathVariable Integer id ){
        commandeFournisseurService.delete(id);

    }


    /**
     * modifier une commade par id
     * @param id
     * @param commandeFournisseur
     * @return
     */
    @PutMapping("/{id}")
    public  CommandeFournisseurDto modifier(@PathVariable Integer id , @RequestBody CommandeFournisseur  commandeFournisseur){
        CommandeFournisseur old = commandeFournisseurService.getById(id);
        if (old != null) {
            commandeFournisseur.setId(id);
            return  new CommandeFournisseurDto(commandeFournisseurService.save(commandeFournisseur));

        }
        return  new CommandeFournisseurDto(commandeFournisseurService.save(commandeFournisseur));
    }




}
