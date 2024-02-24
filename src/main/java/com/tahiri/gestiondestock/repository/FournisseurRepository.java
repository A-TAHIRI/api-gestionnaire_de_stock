package com.tahiri.gestiondestock.repository;

import com.tahiri.gestiondestock.model.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, Integer> {
}
