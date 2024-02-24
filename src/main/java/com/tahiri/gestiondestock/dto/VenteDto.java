package com.tahiri.gestiondestock.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tahiri.gestiondestock.model.Vente;

import lombok.Data;

import java.time.Instant;
import java.util.List;


@Data

public class VenteDto {
    private Integer id;
    private String reference;
    private Instant dateVente;
    private Integer idEntreprise;


    @JsonIgnore
    private List<LigneVenteDto> ligneVentes;


    public VenteDto (Vente vente) {
        this.id=vente.getId();
        this.reference=vente.getReference();
        this.dateVente=vente.getDateVente();
        this.idEntreprise=vente.getIdEntreprise();


    }


}
