package com.tahiri.gestiondestock.controller;


import com.tahiri.gestiondestock.dto.ClientDto;
import com.tahiri.gestiondestock.dto.UtilisateurDto;
import com.tahiri.gestiondestock.interceptor.Interceptor;
import com.tahiri.gestiondestock.model.Client;
import com.tahiri.gestiondestock.model.Utilisateur;
import com.tahiri.gestiondestock.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tahiri.gestiondestock.utils.constant.CLIENT_ENDPOINT;

@RestController
@RequestMapping(CLIENT_ENDPOINT)

@Slf4j
public class ClientController {
    @Autowired
    private ClientService clientService;
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
    String identreprise = MDC.get("idEntreprise");

    /**
     * recupirer tous les clients
     * @return
     */
    @GetMapping("")
    public List<ClientDto> tousClients() {
        List<Client> clients = clientService.getAll();
        List<ClientDto> clientDtos= new ArrayList<>();
        for( Client c : clients){

            clientDtos.add(new ClientDto(c));
        }
        return  clientDtos;
    }

    /**
     * recupirer le client de l'id
     * @param id
     * @return un client
     */
    @GetMapping("/{id}")
    public  ClientDto unClient(@PathVariable Integer id ){

        return new ClientDto( clientService.getById(id));
    }


    /**
     * enregistrer un client dans la bdd
     * @param client
     * @return
     */
    @PostMapping("")
    public  ClientDto enregistrer( @RequestBody Client client){
        return  new ClientDto( clientService.save(client));

    }

    /**
     * supprimer le client par id
     * @param id
     */

    @DeleteMapping("/{id}")
    public void  supprimer( @PathVariable Integer id){
        clientService.delete(id);

    }


    /**
     * modifier le client
     * @param id
     * @param client
     * @return
     */
    @PutMapping("/{id}")
    public  ClientDto modifier(@PathVariable Integer id , @RequestBody Client client ){
        Client olclient = clientService.getById(id);
        if (olclient != null) {
            client.setId(id);

            return  new ClientDto(clientService.save(client));
        }
        return  new ClientDto(clientService.save(client));
    }


    @GetMapping("/clients")
    public Page<ClientDto> getUsers(@RequestParam(defaultValue = "",required = false,name = "nom") String name,
                                         @RequestParam Optional<Integer> page,
                                         @RequestParam Optional<Integer> size){

        Page<Client>   clientPage  =   this.clientService.getclients(name,page.orElse(0),size.orElse(10));

        Page<ClientDto> clientDtoPage =clientPage.map(client -> {
            ClientDto clientDto =new ClientDto(client);
            return clientDto;
        });
        return clientDtoPage;

    }

    /**
     * retourn le nombre des client dans le mois president
     * @return
     */

    @GetMapping("/bymonth")
    public int getByMonth(){
        String identreprise = MDC.get("idEntreprise");
        return clientService.countClientBymouth(identreprise);
    }
    /**
     * retourn le nombre des client dans le mois president
     * @return
     */

    @GetMapping("/bythismonth")
    public int getByThisMonth(){
        String identreprise = MDC.get("idEntreprise");
        return clientService.countClientByThisMouth(identreprise);
    }

    /**
     * retourn le nombre des client dans l'anné actuel
     * @return
     */

    @GetMapping("/byyear")
    public int getByYear(){
        String identreprise = MDC.get("idEntreprise");
        return clientService.countClientByYear(identreprise);
    }
    /**
     * retourn le nombre des client dans l'anné président
     * @return
     */

    @GetMapping("/bylastyear")
    public int getByLastYear(){
        String identreprise = MDC.get("idEntreprise");
        return clientService.countClientByLastYear(identreprise);
    }


    /**
     * retourn le nombre des client le jour actuelle
     * @return
     */

    @GetMapping("/byday")
    public int getByDay(){
        String identreprise = MDC.get("idEntreprise");
        return clientService.countClientByDay(identreprise);
    }
    /**
     * retourn le nombre des client le jour président
     * @return
     */

    @GetMapping("/bylastday")
    public int getByLastDay(){
        String identreprise = MDC.get("idEntreprise");
        return clientService.countClientByLastDay(identreprise);
    }
}
