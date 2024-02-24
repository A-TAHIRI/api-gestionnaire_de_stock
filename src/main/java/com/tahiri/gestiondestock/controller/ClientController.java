package com.tahiri.gestiondestock.controller;


import com.tahiri.gestiondestock.dto.ClientDto;
import com.tahiri.gestiondestock.model.Client;
import com.tahiri.gestiondestock.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.tahiri.gestiondestock.utils.constant.CLIENT_ENDPOINT;

@RestController
@RequestMapping(CLIENT_ENDPOINT)
@CrossOrigin(origins = {"http://localhost:4200","https://monsite.fr"})
public class ClientController {
    @Autowired
    private ClientService clientService;


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
}
