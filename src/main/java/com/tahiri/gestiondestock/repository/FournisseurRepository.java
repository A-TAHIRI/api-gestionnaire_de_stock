package com.tahiri.gestiondestock.repository;

import com.tahiri.gestiondestock.model.Categorie;
import com.tahiri.gestiondestock.model.Fournisseur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, Integer> {
    Page<Fournisseur> findByNomContaining(String name, Pageable pageable);
}
