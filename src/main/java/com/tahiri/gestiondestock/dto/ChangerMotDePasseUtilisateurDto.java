package com.tahiri.gestiondestock.dto;


import lombok.Builder;
import lombok.Data;


@Data
public class ChangerMotDePasseUtilisateurDto {

  private Integer id;

  private String motDePasse;

  private String confirmMotDePasse;

}
