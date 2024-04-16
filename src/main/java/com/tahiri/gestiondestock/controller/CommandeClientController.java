package com.tahiri.gestiondestock.controller;


import com.tahiri.gestiondestock.dto.CommandeClientDto;
import com.tahiri.gestiondestock.dto.LigneCommadeClientDto;
import com.tahiri.gestiondestock.dto.UtilisateurDto;
import com.tahiri.gestiondestock.model.CommandeClient;
import com.tahiri.gestiondestock.model.LigneCommandeClient;
import com.tahiri.gestiondestock.model.Utilisateur;
import com.tahiri.gestiondestock.service.CommandeClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tahiri.gestiondestock.utils.constant.COMMANDE_CLIENT_ENDPOINT;

@RestController
@RequestMapping(COMMANDE_CLIENT_ENDPOINT)
@CrossOrigin(origins = {"http://localhost:4200","https://monsite.fr"})
public class CommandeClientController {

    @Autowired
    private CommandeClientService commandeClientService;


    /**
     * recupirer tous les commades clients
     * @return
     */
    @GetMapping("")
    public List<CommandeClientDto> tousLesCommades(){
        List<CommandeClientDto> clientDtos = new ArrayList<>();
        List<CommandeClient> commandeClients = commandeClientService.getAll();
        for ( CommandeClient c :commandeClients){
        clientDtos.add( new CommandeClientDto(c));
        }
        return  clientDtos;
    }


    /**
     * pour recupirer une commade avec son id
     * @param id
     * @return
     */
    @GetMapping("/{id}")

    public  CommandeClientDto uneCommadeClient(@PathVariable Integer id ){
        return  new CommandeClientDto(commandeClientService.getById(id));

    }


    /**
     * ajouter une commade à la basses de données
     * @param commandeClient
     * @return
     */
    @PostMapping("")
    public  CommandeClientDto enregistrer( @RequestBody CommandeClient commandeClient){
         return  new CommandeClientDto(commandeClientService.save(commandeClient));
    }


    @DeleteMapping("/{id}")
    public  void  supprimer( @PathVariable Integer id ){
        commandeClientService.delete(id);

    }


    /**
     * modifier une commade par id
     * @param id
     * @param commandeClient
     * @return
     */
    @PutMapping("/{id}")
    public  CommandeClientDto modifier(@PathVariable Integer id , @RequestBody CommandeClient  commandeClient){
        CommandeClient oldcommadeclient = commandeClientService.getById(id);
        if (oldcommadeclient != null) {
            commandeClient.setId(id);
            return  new CommandeClientDto(commandeClientService.save(commandeClient));

        }
        return  new  CommandeClientDto(commandeClientService.save(commandeClient));
    }

    @GetMapping( "/lignesCommande/{idCommande}")
    List<LigneCommadeClientDto> findAllLignesCommandesClientByCommandeFournisseurId(@PathVariable("idCommande") Integer idCommande){
        List <LigneCommandeClient> liste =  commandeClientService.findAllLignesCommandesByCommandeClient(idCommande);
        List<LigneCommadeClientDto> dtoList= new ArrayList<>();
        for (LigneCommandeClient lig : liste){
            dtoList.add(new LigneCommadeClientDto(lig));

        }
        return dtoList;
    };

    @GetMapping("/commandes")
    public Page<CommandeClientDto> getcommades(@RequestParam Optional<String> name,
                                         @RequestParam Optional<Integer> page,
                                         @RequestParam Optional<Integer> size){

        Page<CommandeClient>   commandeClientPage  =   this.commandeClientService.getcommandes(name.orElse(""),page.orElse(0),size.orElse(10));

        Page<CommandeClientDto> commandeClientDtoPage =commandeClientPage.map(client -> {
            CommandeClientDto commandeClientDto =new CommandeClientDto(client);
            return commandeClientDto;
        });
        return commandeClientDtoPage;

    }


}
