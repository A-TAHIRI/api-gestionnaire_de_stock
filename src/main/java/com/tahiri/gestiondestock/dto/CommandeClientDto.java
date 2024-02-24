package com.tahiri.gestiondestock.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tahiri.gestiondestock.model.CommandeClient;
import com.tahiri.gestiondestock.model.EtatCommande;

import lombok.Data;

import java.time.Instant;
import java.util.List;


@Data

public class CommandeClientDto {
    private Integer id;
    private String reference;
    private Instant dateCommande;
    private Integer idEntreprise;
    private EtatCommande etatCommande;
    private ClientDto client;

    @JsonIgnore
    private List<LigneCommadeClientDto> ligneCommadeClients;


    public CommandeClientDto (CommandeClient commandeClient) {
        this.id=commandeClient.getId();
        this.reference=commandeClient.getReference();
        this.dateCommande=commandeClient.getDateCommande();
        this.etatCommande=commandeClient.getEtatCommande();
        this.idEntreprise=commandeClient.getIdEntreprise();
        this.client=new ClientDto(commandeClient.getClient());
    }



    public boolean isCommandeLivree() {
        return EtatCommande.LIVREE.equals(this.etatCommande);
    }
}
