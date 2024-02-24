package com.tahiri.gestiondestock.controller;

import com.tahiri.gestiondestock.dto.ClientDto;
import com.tahiri.gestiondestock.dto.VenteDto;
import com.tahiri.gestiondestock.model.Client;
import com.tahiri.gestiondestock.model.Vente;
import com.tahiri.gestiondestock.service.VenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.tahiri.gestiondestock.utils.constant.VENTE_ENDPOINT;

@RestController
@RequestMapping(VENTE_ENDPOINT)
@CrossOrigin(origins = {"http://localhost:4200","https://monsite.fr"})
public class VenteController {

    @Autowired
    private  VenteService venteService;


    /**
     * recupirer tous les ventes
     * @return
     */
    @GetMapping("")
    public List<VenteDto> tousVents() {
        List<Vente> ventes = venteService.getdAll();
        List<VenteDto> venteDtos= new ArrayList<>();
        for( Vente v : ventes){
            venteDtos.add(new VenteDto(v));
        }
        return  venteDtos;
    }

    /**
     * recupirer la vente de l'id
     * @param id
     * @return un vent
     */
    @GetMapping("/{id}")
    public  VenteDto unevente(@PathVariable Integer id ){

        return new VenteDto( venteService.getById(id));
    }



    /**
     * enregistrer une vente dans la bdd
     * @param vente
     * @return
     */
    @PostMapping("")
    public  VenteDto enregistrer( @RequestBody Vente vente){
        return  new VenteDto( venteService.save(vente));

    }

    /**
     * supprimer la vente par id
     * @param id
     */

    @DeleteMapping("/{id}")
    public void  supprimer( @PathVariable Integer id){
        venteService.delete(id);

    }

    /**
     * modifier la vente
     * @param id
     * @param vente
     * @return
     */
    @PutMapping("/{id}")
    public  VenteDto modifier(@PathVariable Integer id , @RequestBody Vente vente ){
        Vente old = venteService.getById(id);
        if (old != null) {
            vente.setId(id);

            return  new VenteDto(venteService.save(vente));
        }
        return  new VenteDto(venteService.save(vente));
    }


}
