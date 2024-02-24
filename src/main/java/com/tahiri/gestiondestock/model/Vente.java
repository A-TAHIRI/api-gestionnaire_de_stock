package com.tahiri.gestiondestock.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Data

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Vente")

public class Vente extends  AbstractEntity{


    @Column(name = "reference")
      private  String reference;

    @Column(name = "dateVente")
    private Instant dateVente;

    @Column(name = "identreprise")
    private Integer idEntreprise;


    @OneToMany(mappedBy = "vente")
    private List<LigneVente> ligneVentes;




}
