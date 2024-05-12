package com.tahiri.gestiondestock.controller;


import com.tahiri.gestiondestock.dto.CategorieDto;
import com.tahiri.gestiondestock.dto.UtilisateurDto;
import com.tahiri.gestiondestock.model.Categorie;
import com.tahiri.gestiondestock.model.Utilisateur;
import com.tahiri.gestiondestock.service.CategorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tahiri.gestiondestock.utils.constant.CATEGORIE_ENDPOINT;

@RestController
@RequestMapping(CATEGORIE_ENDPOINT)
@CrossOrigin(origins = {"http://localhost:4200","https://monsite.fr"})
public class CategorieController {
    @Autowired
    private CategorieService categorieService;

    /**
     * method pour récupirer tous les categories
     * @return
     */

    @GetMapping("")
    public List<CategorieDto> touLescategiries(){
        List<Categorie> list= categorieService.getAll();
        List<CategorieDto> dtoList= new ArrayList<>();
        for (Categorie c : list){

            dtoList.add(new CategorieDto(c));
        }
        return dtoList;
    }


    /**
     * method pour recupirer une categorie
     * @param id
     * @return une categorie
     */
    @GetMapping("/{id}")
    public CategorieDto uneCategorie(@PathVariable Integer id ){
        return new CategorieDto(categorieService.getById(id));

    }


    /**
     * method pour ajouter une categorie
     * @param categorie
     * @return catrgorie
     */

    @PostMapping("")
    public  CategorieDto enregestrer( @RequestBody Categorie categorie ){
        return  new CategorieDto(categorieService.save(categorie));

    }


    /**
     * supremer une categorie
     * @param id
     */
    @DeleteMapping("/{id}")
    public  void  supprimer(@PathVariable Integer id ){
        categorieService.delete(id);

    }

    /**
     * modifier une categorie
     * @param id
     * @param categorie
     * @return
     */
    @PutMapping("/{id}")
    public  CategorieDto modifier(@PathVariable Integer id  , @RequestBody Categorie categorie){
        Categorie oldcategorie = categorieService.getById(id);

        if (oldcategorie != null) {
            categorie.setId(id);
            return  new CategorieDto(categorieService.save(categorie));
        }
        return  new CategorieDto(categorieService.save(categorie));

    }

    @GetMapping("/cate")
    public Page<CategorieDto> getCategories(@RequestParam Optional<String> name,
                                         @RequestParam Optional<Integer> page,
                                         @RequestParam Optional<Integer> size){

        Page<Categorie>   categoriePage  =   this.categorieService.getCategories(name.orElse(""),page.orElse(0),size.orElse(10));

        Page<CategorieDto> categorieDtoPage =categoriePage.map(categorie -> {
            CategorieDto categorieDto =new CategorieDto(categorie);
            return categorieDto;
        });
        return categorieDtoPage;

    }







}
