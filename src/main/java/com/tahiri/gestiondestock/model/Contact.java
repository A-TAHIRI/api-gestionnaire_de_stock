package com.tahiri.gestiondestock.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="Contact")
public class Contact extends  AbstractEntity {

    @Column(name = "email")
    private  String email;
    @Column(name = "sujet")
    private  String sujet;
    @Column(name = "message")
    private  String message;
}
