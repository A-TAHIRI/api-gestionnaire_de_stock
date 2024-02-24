package com.tahiri.gestiondestock.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;


@Entity
@Data

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Categorie")
public class Categorie extends  AbstractEntity {

    @Column(name = "code")
    private  String code;

    @Column(name = "designation")
    private  String designation;


    @Column(name = "identreprise")
    private Integer idEntreprise;

    @OneToMany(mappedBy = "categorie")
    private List<Article> articles;



}
