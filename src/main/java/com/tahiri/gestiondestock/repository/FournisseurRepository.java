package com.tahiri.gestiondestock.repository;

import com.tahiri.gestiondestock.model.Categorie;
import com.tahiri.gestiondestock.model.Fournisseur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;


@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, Integer> {
    Page<Fournisseur> findByNomContaining(String name, Pageable pageable);


    /**
     * mois président
     * @return
     */
    @Query("SELECT COALESCE(COUNT(f),0) FROM Fournisseur f WHERE YEAR(f.createDate) = YEAR(CURRENT_DATE) AND MONTH(f.createDate) = (MONTH(CURRENT_DATE )-1)")
    int countFournisseursByMonthAndYear();


    /**
     * mois actuel
     * @return
     */
    @Query("SELECT COALESCE(COUNT(f),0) FROM Fournisseur f WHERE YEAR(f.createDate) = YEAR(CURRENT_DATE) AND MONTH(f.createDate) = MONTH(CURRENT_DATE )")
    int countFournisseursByThisMonthAndYear();

    /**
     *cette année
     * @return
     */
    @Query("SELECT COALESCE(COUNT(f),0) FROM Fournisseur f WHERE YEAR(f.createDate) = YEAR(CURRENT_DATE)")
    int countFournisseursByYear();

    /**
     * année président
     * @return
     */
    @Query("SELECT COALESCE(COUNT(f),0) FROM Fournisseur f WHERE YEAR(f.createDate) = (YEAR(CURRENT_DATE)-1)")
    int countFournisseursByLastYear();


    /**
     * aujour'huit
     * @return
     */
    @Query("SELECT COALESCE(COUNT(f),0) FROM Fournisseur f WHERE DATE(f.createDate) = CURRENT_DATE")
    int countFournisseursByDay();

    /**
     * hier
     * @return
     */

    @Query("SELECT COALESCE(COUNT(f),0) FROM Utilisateur f WHERE DATE(f.createDate) = :yesterdayDate")
    int countUtilisateursByYesterday(@Param("yesterdayDate") Timestamp yesterdayDate);

}
