package com.tahiri.gestiondestock.repository;

import com.tahiri.gestiondestock.model.CommandeClient;
import com.tahiri.gestiondestock.model.CommandeFournisseur;
import com.tahiri.gestiondestock.model.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CommandeClientRepository extends JpaRepository<CommandeClient, Integer> {


    Optional<CommandeClient> findByReference(String reference);

    List<CommandeClient> findByClient_Id(Integer id);

    Page<CommandeClient> findByReferenceContaining(String nom, Pageable pageable);
}
