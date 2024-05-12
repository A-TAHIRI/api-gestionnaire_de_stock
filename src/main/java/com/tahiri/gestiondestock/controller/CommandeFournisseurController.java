package com.tahiri.gestiondestock.controller;


import com.tahiri.gestiondestock.dto.CommandeClientDto;
import com.tahiri.gestiondestock.dto.CommandeFournisseurDto;
import com.tahiri.gestiondestock.dto.LigneCommadeFournisseurDto;
import com.tahiri.gestiondestock.model.CommandeClient;
import com.tahiri.gestiondestock.model.CommandeClientStats;
import com.tahiri.gestiondestock.model.CommandeFournisseur;
import com.tahiri.gestiondestock.model.LigneCommandeFournisseur;
import com.tahiri.gestiondestock.service.CommandeFournisseurService;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @GetMapping( "/lignesCommande/{idCommande}")
    List<LigneCommadeFournisseurDto> findAllLignesCommandesFournisseurByCommandeFournisseurId(@PathVariable("idCommande") Integer idCommande){
      List <LigneCommandeFournisseur> liste =  commandeFournisseurService.findAllLignesCommandesFournisseurByCommandeFournisseurId(idCommande);
      List<LigneCommadeFournisseurDto> dtoList= new ArrayList<>();
     for (LigneCommandeFournisseur l :liste){
         dtoList.add(new LigneCommadeFournisseurDto(l));

        }
      return dtoList;
    };

    @GetMapping("/commandes")
    public Page<CommandeFournisseurDto> getcommandes(@RequestParam Optional<String> name,
                                            @RequestParam Optional<Integer> page,
                                            @RequestParam Optional<Integer> size){

        Page<CommandeFournisseur>   commandeFournisseurPage  =   this.commandeFournisseurService.getcommandes(name.orElse(""),page.orElse(0),size.orElse(10));

        Page<CommandeFournisseurDto> commandeFournisseurDtoPage =commandeFournisseurPage.map(fournisseur -> {
            CommandeFournisseurDto commandeFournisseurDto =new CommandeFournisseurDto(fournisseur);
            return commandeFournisseurDto;
        });
        return commandeFournisseurDtoPage;

    }
    /**
     * Methode pour supprimer une ligne de commande
     * @param id
     */
    @DeleteMapping("/lignecommande/delet")
    public  void supprimerligne(@RequestParam Integer id){
        this.commandeFournisseurService.delet(id);
    }

/*******************************************nombre de commande******************************************************/

    /**
     * retourn le nombre des CommandeFournisseur dans le mois president
     * @return
     */

    @GetMapping("/bymonth")
    public int getByMonth(){
        return commandeFournisseurService.countCommandeFournisseurBymouth();
    }
    /**
     * retourn le nombre des CommandeFournisseur dans le mois actuel
     * @return
     */

    @GetMapping("/bythismonth")
    public int getByThisMonth(){
        return commandeFournisseurService.countCommandeFournisseurByThisMouth();
    }

    /**
     * retourn le nombre des CommandeFournisseur dans l'anné actuel
     * @return
     */

    @GetMapping("/byyear")
    public int getByYear(){
        return commandeFournisseurService.countCommandeFournisseurByYear();
    }
    /**
     * retourn le nombre des CommandeFournisseur dans l'anné président
     * @return
     */

    @GetMapping("/bylastyear")
    public int getByLastYear(){
        return commandeFournisseurService.countCommandeFournisseurByLastDay();
    }


    /**
     * retourn le nombre des CommandeFournisseur le jour actuelle
     * @return
     */

    @GetMapping("/byday")
    public int getByDay(){
        return commandeFournisseurService.countCommandeFournisseurByDay();
    }
    /**
     * retourn le nombre des CommandeFournisseur le jour président
     * @return
     */

    @GetMapping("/bylastday")
    public int getByLastDay(){
        return commandeFournisseurService.countCommandeFournisseurByLastDay();
    }

    /***********************************************revenue****************************************/
    /**
     * retourn le revenue des CommandeFournisseur dans le mois actuel
     * @return
     */

    @GetMapping("/sumbymonth")
    public int getSumByMonth(){
        return commandeFournisseurService.sumCommandeFournisseurBymouth();
    }
    /**
     * retourn le revenue des CommandeFournisseur dans le mois president
     * @return
     */

    @GetMapping("/sumbylastmonth")
    public int getSumByLastMonth(){
        return commandeFournisseurService.sumCommandeFournisseurByLastMouth();
    }

    /**
     * retourn le revenue des CommandeFournisseur dans l'anné actuel
     * @return
     */

    @GetMapping("/sumbyyear")
    public int getSumByYear(){
        return commandeFournisseurService.sumCommandeFournisseurByYear();
    }
    /**
     * retourn le revenue des CommandeFournisseur dans l'anné président
     * @return
     */

    @GetMapping("/sumbylastyear")
    public int getSumByLastYear(){
        return commandeFournisseurService.sumCommandeFournisseurByLastYear();
    }


    /**
     * retourn le revenue des CommandeFournisseur le jour actuelle
     * @return
     */

    @GetMapping("/sumbyday")
    public int getSumByDay(){
        return commandeFournisseurService.sumCommandeFournisseurByDay();
    }
    /**
     * retourn le revenue des CommandeFournisseur le jour président
     * @return
     */

    @GetMapping("/sumbylastday")
    public int getSumByLastDay(){
        return commandeFournisseurService.sumCommandeFournisseurByLastDay();
    }


    /***********************************************CLASSEMENT DES COMMANDES PAR ORDER DESC ************************************************************/

    /**
     * retourn les CommandeFournisseur par order dec totalprix dans le mois actuel
     * @return
     */

    @GetMapping("/cmdorderbytotlbymoth")
    public List<CommandeClientStats>getCmdFrsByMonthByOrderByTotalPrixDesc(){
        String identreprise = MDC.get("idEntreprise");
       return commandeFournisseurService.CmdFrsByMonthByOrderByTotalPrixDesc(identreprise);

    }
    /**
     * retourn les CommandeFournisseur par order dec totalprix dans le mois president
     * @return
     */

    @GetMapping("/cmdorderbytotlbylastmoth")
    public List<CommandeClientStats> getCmdFrsByLastMonthByOrderByTotalPrixDesc() {
        String identreprise = MDC.get("idEntreprise");
        return commandeFournisseurService.CmdFrsByLastMonthByOrderByTotalPrixDesc(identreprise);

    }
    /**
     * retourn les CommandeFournisseur par order dec totalprixdans l'anné actuel
     * @return
     */

    @GetMapping("/cmdorderbytotlbyyear")
    public List<CommandeClientStats> getCmdFrsByYearByOrderByTotalPrixDesc(){
        String identreprise = MDC.get("idEntreprise");
      return   commandeFournisseurService.CmdFrsByYearByOrderByTotalPrixDesc(identreprise);

    }
    /**
     * retourn les CommandeFournisseur par order dec totalprixdans l'anné président
     * @return
     */

    @GetMapping("/cmdorderbytotlbylastyear")
    public List<CommandeClientStats> getCmdFrsByLastYearByOrderByTotalPrixDesc(){
        String identreprise = MDC.get("idEntreprise");
        return commandeFournisseurService.CmdFrsByLastYearByOrderByTotalPrixDesc(identreprise);

    }


    /**
     * retourn les CommandeFournisseur par order dec totalprix le jour actuelle
     * @return
     */

    @GetMapping("/cmdorderbytotlbyday")
    public List<CommandeClientStats> getCmdFrsByDayByOrderByTotalPrixDesc(){
        String identreprise = MDC.get("idEntreprise");
        return commandeFournisseurService.CmdFrsByDayByOrderByTotalPrixDesc(identreprise);


    }
    /**
     * retourn les CommandeFournisseur par order dec totalprix le jour président
     * @return
     */

    @GetMapping("/cmdorderbytotlbylastday")
    public List<CommandeClientStats> getCmdFrsByLastDayByOrderByTotalPrixDesc(){
        String identreprise = MDC.get("idEntreprise");
        return   commandeFournisseurService.CmdFrsByLastDayByOrderByTotalPrixDesc(identreprise);


    }

}
