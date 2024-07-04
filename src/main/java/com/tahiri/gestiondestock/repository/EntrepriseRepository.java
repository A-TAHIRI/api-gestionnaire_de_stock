package com.tahiri.gestiondestock.repository;

import com.tahiri.gestiondestock.model.Entreprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntrepriseRepository extends JpaRepository<Entreprise, Integer> {
    Entreprise findByEmail(String email);

    Entreprise findByCodeFiscal(String codeFiscal);

    Entreprise findByNumTel(String numTel);

    Entreprise findByNom(String nom);
}
