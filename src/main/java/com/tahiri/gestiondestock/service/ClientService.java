package com.tahiri.gestiondestock.service;

import com.tahiri.gestiondestock.exception.EntityNotFoundException;
import com.tahiri.gestiondestock.exception.ErrorCodes;
import com.tahiri.gestiondestock.exception.InvalidEntityException;
import com.tahiri.gestiondestock.exception.InvalidOperationException;
import com.tahiri.gestiondestock.model.Client;
import com.tahiri.gestiondestock.model.CommandeClient;
import com.tahiri.gestiondestock.model.Utilisateur;
import com.tahiri.gestiondestock.repository.ClientRepository;
import com.tahiri.gestiondestock.repository.CommandeClientRepository;
import com.tahiri.gestiondestock.validator.ClientValidator;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.data.domain.PageRequest.of;

@Service
@Slf4j
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    CommandeClientRepository commandeClientRepository;


    public Client save(Client client) {
        List<String> errors = ClientValidator.validate(client);
        if (!errors.isEmpty()) {
            log.error("Client is not valid {}", client);
            throw new InvalidEntityException("Le client n'est pas valide", ErrorCodes.CLIENT_NOT_VALID, errors);
        }

        return clientRepository.save(client);
    }

    public List<Client> getAll() {

        return clientRepository.findAll();
    }


    public Client getById(Integer id) {
        if (id == null) {
            log.error("Client ID is null");
            return null;
        }
        return clientRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun Client avec l'ID = " + id + " n' ete trouve dans la BDD",
                        ErrorCodes.CLIENT_NOT_FOUND)
        );
    }

    public void delete(Integer id) {

        if (id == null) {
            log.error("Client ID is null");
            return;
        }

        List<CommandeClient> commandeClients= commandeClientRepository.findByClient_Id(id);
        if (!commandeClients.isEmpty()) {
            throw new InvalidOperationException("Impossible de supprimer un client qui a deja des commande clients",
                    ErrorCodes.CLIENT_ALREADY_IN_USE);
        }
        clientRepository.deleteById(id);
    }

    /**
     * Service pour recupirer les client par page et par recherche
     * @param nom
     * @param page
     * @param size
     * @return
     */
    public Page<Client> getclients(String nom, int page, int size){
        String identreprise = MDC.get("idEntreprise");
        return clientRepository.findByNomContainingAndIdEntreprise(nom,Integer.valueOf(identreprise) , of(page,size));
    }


    /**
     * Service qui retourne le nombre des client de  mois prisident
     * @return
     */

    public int countClientBymouth(String identreprise){
        return this.clientRepository.countClientsByMonthAndYear(identreprise);
    }

    /**
     * Service qui retourne le nombre des client de  mois actuel
     * @return
     */

    public int countClientByThisMouth(String identreprise){
        return this.clientRepository.countClientsByThisMonthAndYear(identreprise);
    }


    /**
     * Service qui retourne le nombre des client de cette année
     * @return
     */
    public int countClientByYear(String identreprise){
        return this.clientRepository.countClientsByYear(identreprise);
    }

    /**
     * Service qui retourne le nombre des client de l' année président
     * @return
     */
    public int countClientByLastYear(String identreprise){
        return this.clientRepository.countClientsByLastYear(identreprise);
    }



    /**
     * Service qui retourne le nombre des client de aujourd'huit
     * @return
     */
    public int countClientByDay(String identreprise){
        return this.clientRepository.countClientsByDay(identreprise);
    }
    /**
     * Service qui retourne le nombre des client d'hier
     * @return
     */
    public int countClientByLastDay(String identreprise){
        LocalDate yesterdayDate = LocalDate.now().minusDays(1);
        Timestamp yesterdayTimestamp = Timestamp.valueOf(yesterdayDate.atStartOfDay());
        return this.clientRepository.countClientsByYesterday(yesterdayTimestamp,identreprise);
    }


}
