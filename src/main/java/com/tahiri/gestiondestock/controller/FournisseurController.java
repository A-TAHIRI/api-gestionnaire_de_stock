package com.tahiri.gestiondestock.controller;

import com.tahiri.gestiondestock.dto.ClientDto;
import com.tahiri.gestiondestock.dto.EntrepriseDto;
import com.tahiri.gestiondestock.dto.FournisseurDto;
import com.tahiri.gestiondestock.model.Client;
import com.tahiri.gestiondestock.model.Entreprise;
import com.tahiri.gestiondestock.model.Fournisseur;
import com.tahiri.gestiondestock.service.FournisseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.tahiri.gestiondestock.utils.constant.FOURNISSEUR_ENDPOINT;

@RestController
@RequestMapping(FOURNISSEUR_ENDPOINT)
@CrossOrigin(origins = {"http://localhost:4200","https://monsite.fr"})
public class FournisseurController {
    @Autowired
    private FournisseurService fournisseurService;


    /**
     * recupirer tous les entreprises
     * @return
     */
    @GetMapping("")
    public List<FournisseurDto> tousFournisseurs() {
        List<Fournisseur> fournisseurs = fournisseurService.getdAll();
        List<FournisseurDto> fournisseurDtos= new ArrayList<>();
        for( Fournisseur f : fournisseurs){

            fournisseurDtos.add(new FournisseurDto(f));
        }
        return  fournisseurDtos;
    }

    /**
     * recupirer le fournisseur de l'id
     * @param id
     * @return un fournisseur
     */
    @GetMapping("/{id}")
    public  FournisseurDto unFournisseur(@PathVariable Integer id ){

        return new FournisseurDto( fournisseurService.getById(id));
    }


    /**
     * enregistrer un fournisseur dans la bdd
     * @param fournisseur
     * @return
     */
    @PostMapping("")
    public FournisseurDto enregistrer(@RequestBody Fournisseur fournisseur){
        return  new FournisseurDto( fournisseurService.save(fournisseur));

    }

    /**
     * supprimer le fournisseur par id
     * @param id
     */

    @DeleteMapping("/{id}")
    public void  supprimer( @PathVariable Integer id){
        fournisseurService.delete(id);

    }

    /**
     * modifier le fournisseur
     * @param id
     * @param fournisseur
     * @return
     */
    @PutMapping("/{id}")
    public  FournisseurDto modifier(@PathVariable Integer id , @RequestBody Fournisseur fournisseur ){
        Fournisseur old = fournisseurService.getById(id);
        if (old != null) {
            fournisseur.setId(id);

            return  new FournisseurDto(fournisseurService.save(fournisseur));
        }
        return  new FournisseurDto(fournisseurService.save(fournisseur));
    }

}
