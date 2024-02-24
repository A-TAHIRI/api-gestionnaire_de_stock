package com.tahiri.gestiondestock.repository;

import com.tahiri.gestiondestock.model.LigneCommadeClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LigneCommandeClientRepository extends JpaRepository<LigneCommadeClient, Integer> {
    List<LigneCommadeClient> findByArticle_Id(Integer id);

    List<LigneCommadeClient> findByCommandeClient_Id(Integer id);

    List<LigneCommadeClient> findLigneCommadeClientByArticle_Id(Integer idArticle);
}
