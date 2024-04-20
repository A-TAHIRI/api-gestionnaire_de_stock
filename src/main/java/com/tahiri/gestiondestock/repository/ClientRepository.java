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
    Page<Client> findByNomContaining(String name, Pageable pageable);
    /**
     * mois président
     * @return
     */
    @Query("SELECT COALESCE(COUNT(c),0) FROM Client c WHERE YEAR(c.createDate) = YEAR(CURRENT_DATE) AND MONTH(c.createDate) = (MONTH(CURRENT_DATE )-1)")
    int countClientsByMonthAndYear();


    /**
     * mois actuel
     * @return
     */
    @Query("SELECT COALESCE(COUNT(c),0) FROM Client c WHERE YEAR(c.createDate) = YEAR(CURRENT_DATE) AND MONTH(c.createDate) = MONTH(CURRENT_DATE )")
    int countClientsByThisMonthAndYear();

    /**
     *cette année
     * @return
     */
    @Query("SELECT COALESCE(COUNT(c),0) FROM Client c WHERE YEAR(c.createDate) = YEAR(CURRENT_DATE)")
    int countClientsByYear();

    /**
     * année président
     * @return
     */
    @Query("SELECT COALESCE(COUNT(c),0) FROM Client c WHERE YEAR(c.createDate) = (YEAR(CURRENT_DATE)-1)")
    int countClientsByLastYear();


    /**
     * aujour'huit
     * @return
     */
    @Query("SELECT COALESCE(COUNT(c),0) FROM Client c WHERE DATE(c.createDate) = CURRENT_DATE")
    int countClientsByDay();

    /**
     * hier
     * @return
     */

    @Query("SELECT COALESCE(COUNT(c),0) FROM Client c WHERE DATE(c.createDate) = :yesterdayDate")
    int countClientsByYesterday(@Param("yesterdayDate") Timestamp yesterdayDate);

}
