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
    Page<Fournisseur> findByNomContainingAndIdEntreprise(String name,Integer identreprise, Pageable pageable);


    /**
     * mois président
     * @return
     */
    @Query("SELECT COALESCE(COUNT(f),0) FROM Fournisseur f WHERE YEAR(f.createDate) = YEAR(CURRENT_DATE) AND MONTH(f.createDate) = (MONTH(CURRENT_DATE )-1) AND f.idEntreprise = :identreprise")
    int countFournisseursByMonthAndYear(@Param("identreprise") String identreprise);


    /**
     * mois actuel
     * @return
     */
    @Query("SELECT COALESCE(COUNT(f),0) FROM Fournisseur f WHERE YEAR(f.createDate) = YEAR(CURRENT_DATE) AND MONTH(f.createDate) = MONTH(CURRENT_DATE ) AND f.idEntreprise = :identreprise")
    int countFournisseursByThisMonthAndYear(@Param("identreprise") String identreprise);

    /**
     *cette année
     * @return
     */
    @Query("SELECT COALESCE(COUNT(f),0) FROM Fournisseur f WHERE YEAR(f.createDate) = YEAR(CURRENT_DATE) AND f.idEntreprise = :identreprise")
    int countFournisseursByYear(@Param("identreprise") String identreprise);

    /**
     * année président
     * @return
     */
    @Query("SELECT COALESCE(COUNT(f),0) FROM Fournisseur f WHERE YEAR(f.createDate) = (YEAR(CURRENT_DATE)-1) AND f.idEntreprise = :identreprise")
    int countFournisseursByLastYear(@Param("identreprise") String identreprise);


    /**
     * aujour'huit
     * @return
     */
    @Query("SELECT COALESCE(COUNT(f),0) FROM Fournisseur f WHERE DATE(f.createDate) = CURRENT_DATE AND f.idEntreprise = :identreprise")
    int countFournisseursByDay(@Param("identreprise") String identreprise);

    /**
     * hier
     * @return
     */

    @Query("SELECT COALESCE(COUNT(f),0) FROM Fournisseur f WHERE DATE(f.createDate) = :yesterdayDate AND f.idEntreprise = :identreprise")
    int countFournisseursByYesterday(@Param("yesterdayDate") Timestamp yesterdayDate,@Param("identreprise") String identreprise);

}
