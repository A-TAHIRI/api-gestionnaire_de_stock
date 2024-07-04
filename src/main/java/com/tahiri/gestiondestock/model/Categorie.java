package com.tahiri.gestiondestock.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
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

    @Column(name = "image")
    private  String image;


    @Column(name = "identreprise")
    private Integer idEntreprise;

    @OneToMany(mappedBy = "categorie")
    private List<Article> articles;




}
