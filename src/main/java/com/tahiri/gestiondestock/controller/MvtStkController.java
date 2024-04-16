package com.tahiri.gestiondestock.controller;

import com.tahiri.gestiondestock.dto.ClientDto;
import com.tahiri.gestiondestock.dto.MvtStkDto;
import com.tahiri.gestiondestock.dto.UtilisateurDto;
import com.tahiri.gestiondestock.model.Client;
import com.tahiri.gestiondestock.model.MvtStk;
import com.tahiri.gestiondestock.model.Utilisateur;
import com.tahiri.gestiondestock.service.MvtStkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tahiri.gestiondestock.utils.constant.MVTSTK_ENOINT;

@RestController
@RequestMapping(MVTSTK_ENOINT)
@CrossOrigin(origins = {"http://localhost:4200","https://monsite.fr"})
public class MvtStkController {



    @Autowired

    private MvtStkService mvtStkService;

    /**
     * Method pour récupirer tous le mvtstk
     * @return
     */
    @GetMapping("")
    public List<MvtStkDto> mvtstk() {
        List<MvtStk> mvtstks = mvtStkService.getAll();
        List<MvtStkDto> mvtStkDtos = new ArrayList<>();
        for (MvtStk m : mvtstks) {

            mvtStkDtos.add(new MvtStkDto(m));
        }
        return mvtStkDtos;
    }

    /**
     * Method pur récupirer mvtstk regrouper by id Article
     * @return
     */
    @GetMapping("/byarticle")
    public List<MvtStkDto> mvtstkByArticle() {
        List<MvtStk> mvtstks = mvtStkService.mvtStkGrpouByArticle();
        List<MvtStkDto> mvtStkDtos = new ArrayList<>();
        for (MvtStk m : mvtstks) {

            mvtStkDtos.add(new MvtStkDto(m));
        }
        return mvtStkDtos;
    }


    /**
     * Method pour récupirer tous mvtstk par article(id)
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public List<MvtStkDto> mvtstkByArticle(@PathVariable Integer id) {
        List<MvtStk> mvtStks = mvtStkService.mvtStkArticle(id);
        List<MvtStkDto> mvtStkDtos= new ArrayList<>();

        for (MvtStk m : mvtStks){
            mvtStkDtos.add(new MvtStkDto(m));
        }
        return mvtStkDtos;
    }

    /**
     * Method pour récupirer le stock réel d'un article
     * @param id
     * @return
     */
    @GetMapping("/stock/{id}")
    public BigDecimal stockReel(@PathVariable  Integer id){
        return this.mvtStkService.stockReelArticle(id);
    }


    @GetMapping("/mvtstkcontaining")
    public Page<MvtStkDto> getmvtskbyarticle(@RequestParam Optional<String> name,
                                         @RequestParam Optional<Integer> page,
                                         @RequestParam Optional<Integer> size){

        Page<MvtStk>   mvtStkPage  =   this.mvtStkService.getmvtstkbyarticle(name.orElse(""),page.orElse(0),size.orElse(10));

        Page<MvtStkDto> mvtStkDtoPage =mvtStkPage.map(mvtStk -> {
            MvtStkDto mvtStkDto =new MvtStkDto(mvtStk);
            return mvtStkDto;
        });
        return mvtStkDtoPage;

    }
}
