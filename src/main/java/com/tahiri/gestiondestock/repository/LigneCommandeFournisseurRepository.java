package com.tahiri.gestiondestock.repository;

import com.tahiri.gestiondestock.model.LigneCommandeFournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LigneCommandeFournisseurRepository extends JpaRepository<LigneCommandeFournisseur, Integer> {
    List<LigneCommandeFournisseur> findByArticle_Id(Integer id);

    List<LigneCommandeFournisseur> findByCommandeFournisseur_Id(Integer id);
}
