package com.tahiri.gestiondestock.dto;


import com.tahiri.gestiondestock.model.Adresse;
import com.tahiri.gestiondestock.model.Entreprise;
import com.tahiri.gestiondestock.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestDto {
    private  Integer id;
    private  String nom;
    private  String prenom;
    private Adresse adresse;
    private  String photo;
    private  String email;
    private  String numTel;
    private Date dateDeNaissance;
    private String mdp;
    private String token;

    private Entreprise entreprise;

    private List<Role> roles;

}
