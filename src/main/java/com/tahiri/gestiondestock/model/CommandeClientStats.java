package com.tahiri.gestiondestock.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommandeClientStats {
    Integer id;
    String nom;
    String prenom;
    String status;
    Float total;
    String designation;
}
