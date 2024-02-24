package com.tahiri.gestiondestock.repository;

import com.tahiri.gestiondestock.model.Vente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface VenteRepository  extends JpaRepository<Vente , Integer> {
    Optional<Vente> findByReference(String reference);
}
