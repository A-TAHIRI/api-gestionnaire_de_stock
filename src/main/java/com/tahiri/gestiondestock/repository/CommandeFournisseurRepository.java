package com.tahiri.gestiondestock.repository;

import com.tahiri.gestiondestock.model.CommandeFournisseur;
import com.tahiri.gestiondestock.model.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CommandeFournisseurRepository extends JpaRepository<CommandeFournisseur, Integer> {
    Optional<CommandeFournisseur> findByReference(String reference);

    List<CommandeFournisseur> findByFournisseur_Id(Integer id);

    Page<CommandeFournisseur> findByReferenceContaining(String nom, Pageable pageable);
}
