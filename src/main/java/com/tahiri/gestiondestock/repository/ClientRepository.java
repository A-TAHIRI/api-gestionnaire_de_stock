package com.tahiri.gestiondestock.repository;


import com.tahiri.gestiondestock.model.Client;
import com.tahiri.gestiondestock.model.Fournisseur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Page<Client> findByNomContainingAndIdEntreprise(String name,Integer identreprise, Pageable pageable);
    /**
     * mois président
     * @return
     */
    @Query("SELECT COALESCE(COUNT(c),0) FROM Client c WHERE YEAR(c.createDate) = YEAR(CURRENT_DATE) AND MONTH(c.createDate) = (MONTH(CURRENT_DATE )-1) AND c.idEntreprise = :identreprise")
    int countClientsByMonthAndYear(@Param("identreprise") String identreprise);


    /**
     * mois actuel
     * @return
     */
    @Query("SELECT COALESCE(COUNT(c),0) FROM Client c WHERE YEAR(c.createDate) = YEAR(CURRENT_DATE) AND MONTH(c.createDate) = MONTH(CURRENT_DATE ) AND c.idEntreprise = :identreprise")
    int countClientsByThisMonthAndYear(@Param("identreprise") String identreprise);

    /**
     *cette année
     * @return
     */
    @Query("SELECT COALESCE(COUNT(c),0) FROM Client c WHERE YEAR(c.createDate) = YEAR(CURRENT_DATE) AND c.idEntreprise = :identreprise")
    int countClientsByYear(@Param("identreprise") String identreprise);

    /**
     * année président
     * @return
     */
    @Query("SELECT COALESCE(COUNT(c),0) FROM Client c WHERE YEAR(c.createDate) = (YEAR(CURRENT_DATE)-1) AND c.idEntreprise = :identreprise")
    int countClientsByLastYear(@Param("identreprise") String identreprise);


    /**
     * aujour'huit
     * @return
     */
    @Query("SELECT COALESCE(COUNT(c),0) FROM Client c WHERE DATE(c.createDate) = CURRENT_DATE AND c.idEntreprise = :identreprise")
    int countClientsByDay(@Param("identreprise") String identreprise);

    /**
     * hier
     * @return
     */

    @Query("SELECT COALESCE(COUNT(c),0) FROM Client c WHERE DATE(c.createDate) = :yesterdayDate AND c.idEntreprise = :identreprise")
    int countClientsByYesterday(@Param("yesterdayDate") Timestamp yesterdayDate,@Param("identreprise") String identreprise);

}
