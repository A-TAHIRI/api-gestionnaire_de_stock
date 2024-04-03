package com.tahiri.gestiondestock.repository;

import com.tahiri.gestiondestock.model.LigneCommandeClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LigneCommandeClientRepository extends JpaRepository<LigneCommandeClient, Integer> {
    List<LigneCommandeClient> findByArticle_Id(Integer id);

    List<LigneCommandeClient> findByCommandeClient_Id(Integer id);

    List<LigneCommandeClient> findLigneCommadeClientByArticle_Id(Integer idArticle);

    List<LigneCommandeClient> findLigneCommandeClientByArticle_Id(Integer idArticle);
}
