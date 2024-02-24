package com.tahiri.gestiondestock.repository;

import com.tahiri.gestiondestock.model.LigneVente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LigneVenteRepository extends JpaRepository<LigneVente , Integer> {

    List<LigneVente> findByArticle_Id(Integer id);

    List<LigneVente> findLigneVenteByArticle_Id(Integer id);

    List<LigneVente> findByVente_Id(Integer id);
}
