package com.tahiri.gestiondestock.controller;


import com.tahiri.gestiondestock.dto.ArticleStatsDto;
import com.tahiri.gestiondestock.dto.CommandeClientDto;
import com.tahiri.gestiondestock.dto.LigneCommadeClientDto;
import com.tahiri.gestiondestock.dto.UtilisateurDto;
import com.tahiri.gestiondestock.model.*;
import com.tahiri.gestiondestock.service.CommandeClientService;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tahiri.gestiondestock.utils.constant.COMMANDE_CLIENT_ENDPOINT;

@RestController
@RequestMapping(COMMANDE_CLIENT_ENDPOINT)
public class CommandeClientController {

    @Autowired
    private CommandeClientService commandeClientService;


    /**
     * recupirer tous les commades clients
     *
     * @return
     */
    @GetMapping("")
    public List<CommandeClientDto> tousLesCommades() {
        List<CommandeClientDto> clientDtos = new ArrayList<>();
        List<CommandeClient> commandeClients = commandeClientService.getAll();
        for (CommandeClient c : commandeClients) {
            clientDtos.add(new CommandeClientDto(c));
        }
        return clientDtos;
    }


    /**
     * pour recupirer une commade avec son id
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")

    public CommandeClientDto uneCommadeClient(@PathVariable Integer id) {
        return new CommandeClientDto(commandeClientService.getById(id));

    }


    /**
     * ajouter une commade à la basses de données
     *
     * @param commandeClient
     * @return
     */
    @PostMapping("")
    public CommandeClientDto enregistrer(@RequestBody CommandeClient commandeClient) {
        return new CommandeClientDto(commandeClientService.save(commandeClient));
    }

    /**
     * supprimer une commande
     * @param id
     */
    @DeleteMapping("/{id}")
    public void supprimer(@PathVariable Integer id) {
        commandeClientService.delete(id);

    }


    /**
     * modifier une commade par id
     *
     * @param id
     * @param commandeClient
     * @return
     */
    @PutMapping("/{id}")
    public CommandeClientDto modifier(@PathVariable Integer id, @RequestBody CommandeClient commandeClient) {
        CommandeClient oldcommadeclient = commandeClientService.getById(id);
        if (oldcommadeclient != null) {
            commandeClient.setId(id);
            return new CommandeClientDto(commandeClientService.save(commandeClient));

        }
        return new CommandeClientDto(commandeClientService.save(commandeClient));
    }

    /**
     * retourne  les ligneCommade par id de la commade
     * @param idCommande
     * @return
     */
    @GetMapping("/lignesCommande/{idCommande}")
    List<LigneCommadeClientDto> findAllLignesCommandesClientByCommandeFournisseurId(@PathVariable("idCommande") Integer idCommande) {
        List<LigneCommandeClient> liste = commandeClientService.findAllLignesCommandesByCommandeClient(idCommande);
        List<LigneCommadeClientDto> dtoList = new ArrayList<>();
        for (LigneCommandeClient lig : liste) {
            dtoList.add(new LigneCommadeClientDto(lig));

        }
        return dtoList;
    }

    /**
     * recuppirer les commade par les parametre
     * @param name
     * @param page
     * @param size
     * @return
     */

    @GetMapping("/commandes")
    public Page<CommandeClientDto> getcommades(@RequestParam(defaultValue = "",required = false,name = "nom") String name,
                                               @RequestParam Optional<Integer> page,
                                               @RequestParam Optional<Integer> size) {

        Page<CommandeClient> commandeClientPage = this.commandeClientService.getcommandes(name, page.orElse(0), size.orElse(10));

        Page<CommandeClientDto> commandeClientDtoPage = commandeClientPage.map(client -> {
            CommandeClientDto commandeClientDto = new CommandeClientDto(client);
            return commandeClientDto;
        });
        return commandeClientDtoPage;

    }

    /**
     * Methode pour supprimer une ligne de commande
     * @param id
     */
    @DeleteMapping("/lignecommande/delet")
       public  void supprimerligne(@RequestParam  Integer id){
         this.commandeClientService.delet(id);
    }



    /******************************************************nombre de commande*************************************************************************************/


    /**
     * retourn le nombre des CommandeClient dans le mois president
     *
     * @return
     */

    @GetMapping("/bymonth")
    public int getByMonth() {
        return commandeClientService.countCommandeClientBymouth();
    }

    /**
     * retourn le nombre des CommandeClient dans le mois actuel
     *
     * @return
     */

    @GetMapping("/bythismonth")
    public int getByThisMonth() {
        return commandeClientService.countCommandeClientByThisMouth();
    }

    /**
     * retourn le nombre des CommandeClient dans l'anné actuel
     *
     * @return
     */

    @GetMapping("/byyear")
    public int getByYear() {
        return commandeClientService.countCommandeClientByYear();
    }

    /**
     * retourn le nombre des CommandeClient dans l'anné président
     *
     * @return
     */

    @GetMapping("/bylastyear")
    public int getByLastYear() {
        return commandeClientService.countCommandeClientByLastYear();
    }


    /**
     * retourn le nombre des CommandeClient le jour actuelle
     *
     * @return
     */

    @GetMapping("/byday")
    public int getByDay() {
        return commandeClientService.countCommandeClientByDay();
    }

    /**
     * retourn le nombre des CommandeClient le jour président
     *
     * @return
     */

    @GetMapping("/bylastday")
    public int getByLastDay() {
        return commandeClientService.countCommandeClientByLastDay();
    }

/************************** revenue **********************************/


    /**
     * retourn le nombre des CommandeClient dans le mois actuel
     *
     * @return
     */

    @GetMapping("/sumbymonth")
    public int getSumByMonth() {
        return commandeClientService.sumCommandeClientBymouth();
    }

    /**
     * retourn le nombre des CommandeClient dans le mois president
     *
     * @return
     */

    @GetMapping("/sumbylastmonth")
    public int getSumByLastMonth() {
        return commandeClientService.sumCommandeClientByLastMouth();
    }

    /**
     * retourn le nombre des CommandeClient dans l'anné actuel
     *
     * @return
     */

    @GetMapping("/sumbyyear")
    public int getSumByYear() {
        return commandeClientService.sumCommandeClientByYear();
    }

    /**
     * retourn le nombre des CommandeClient dans l'anné président
     *
     * @return
     */

    @GetMapping("/sumbylastyear")
    public int getSumByLastYear() {
        return commandeClientService.sumCommandeClientByLastYear();
    }


    /**
     * retourn le nombre des CommandeClient le jour actuelle
     *
     * @return
     */

    @GetMapping("/sumbyday")
    public int getSumByDay() {
        return commandeClientService.sumCommandeClientByDay();
    }

    /**
     * retourn le nombre des CommandeClient le jour président
     *
     * @return
     */

    @GetMapping("/sumbylastday")
    public int getSumByLastDay() {
        String identreprise = MDC.get("idEntreprise");
        return commandeClientService.sumCommandeClientByLastDay();
    }


/***********************************************CLASSEMENT DES COMMANDES PAR ORDER DESC TOTLPRIX ************************************************************/


    /**
     * retourn les CommandeClients par order dec totalprix dans le mois actuel
     *
     * @return
     */

    @GetMapping("/cmdorderbytotlbymoth")
    public List<CommandeClientStats> getCmdCltByMonthByOrderByTotalPrixDesc() {
        String identreprise = MDC.get("idEntreprise");
       return commandeClientService.CmdCltByMonthByOrderByTotalPrixDesc(identreprise);
    }

    /**
     * retourn les CommandeClients par order dec totalprix dans le mois president
     *
     * @return
     */

    @GetMapping("/cmdorderbytotlbylastmoth")
    public List<CommandeClientStats> getCmdCltByLastMonthByOrderByTotalPrixDesc() {
        String identreprise = MDC.get("idEntreprise");
     return  this.commandeClientService.CmdCltByLastMonthByOrderByTotalPrixDesc(identreprise);
    }

    /**
     * retourn les CommandeClients par order dec totalprixdans l'anné actuel
     *
     * @return
     */

    @GetMapping("/cmdorderbytotlbyyear")
    public List<CommandeClientStats> getCmdCltByYearByOrderByTotalPrixDesc() {
        String identreprise = MDC.get("idEntreprise");
      return   this.commandeClientService.CmdCltByYearByOrderByTotalPrixDesc(identreprise);
    }

    /**
     * retourn les CommandeClients par order dec totalprixdans l'anné président
     *
     * @return
     */

    @GetMapping("/cmdorderbytotlbylastyear")
    public List<CommandeClientStats> getCmdCltByLastYearByOrderByTotalPrixDesc() {
        String identreprise = MDC.get("idEntreprise");
       return   this.commandeClientService.CmdCltByLastYearByOrderByTotalPrixDesc(identreprise);
    }


    /**
     * retourn les CommandeClients par order dec totalprix le jour actuelle
     *
     * @return
     */

    @GetMapping("/cmdorderbytotlbyday")
    public List<CommandeClientStats> getCmdCltByDayByOrderByTotalPrixDesc() {
        String identreprise = MDC.get("idEntreprise");
        return  this.commandeClientService.CmdCltByDayByOrderByTotalPrixDesc(identreprise);

    }

    /**
     * retourn les CommandeClients par order dec totalprix le jour président
     *
     * @return
     */

    @GetMapping("/cmdorderbytotlbylastday")

    public List<CommandeClientStats> getCmdCltByLastDayByOrderByTotalPrixDesc() {
        String identreprise = MDC.get("idEntreprise");
       return  this.commandeClientService.CmdCltByLastDayByOrderByTotalPrixDesc(identreprise);
    }

        /************************************************************ Top articles *****************************************************************************/


    /**
     * Enpoint qui retourne top articles de aujourd'huit
     * @return
     */

        @GetMapping("/toparticlethisday")
        public List<ArticleStats> TopArticlesByCommandesToDay () {
         return this.commandeClientService.getTopArticlesByCommandesToDay();

      }



    /**
     * Enpoint qui retourne top articles de mois actuel
     * @return
     */

    @GetMapping("/toparticlethismonth")
    public List<ArticleStats> TopArticlesByCommandesToMonth () {
      return this.commandeClientService.getTopArticlesByCommandesToMonth();

    }


    /**
     * Enpoint qui retourne top articles de cette année
     * @return
     */

    @GetMapping("/toparticlethisyear")
    public List<ArticleStats> TopArticlesByCommandesToYear () {
    return this.commandeClientService.getTopArticlesByCommandesToYear();

    }


}