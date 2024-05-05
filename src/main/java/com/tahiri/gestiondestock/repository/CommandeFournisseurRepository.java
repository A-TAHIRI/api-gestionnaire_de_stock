package com.tahiri.gestiondestock.repository;

import com.tahiri.gestiondestock.model.CommandeClient;
import com.tahiri.gestiondestock.model.CommandeFournisseur;
import com.tahiri.gestiondestock.model.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;


@Repository
public interface CommandeFournisseurRepository extends JpaRepository<CommandeFournisseur, Integer> {
    Optional<CommandeFournisseur> findByReference(String reference);

    List<CommandeFournisseur> findByFournisseur_Id(Integer id);

    Page<CommandeFournisseur> findByReferenceContaining(String nom, Pageable pageable);

    @Query("SELECT f.etatCommande FROM CommandeFournisseur f WHERE f.id = :id")
    String findEtatCommandeById(@Param("id") Integer id);

/***************************************** nombre de commande *****************************************************************************************/

    /**
     * mois président
     * @return
     */
    @Query("SELECT COALESCE(COUNT(u),0) FROM CommandeFournisseur u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE) AND MONTH(u.createDate) = (MONTH(CURRENT_DATE )-1)")
    int countCommandeFournisseursByMonthAndYear();


    /**
     * mois actuel
     * @return
     */
    @Query("SELECT COALESCE(COUNT(u),0) FROM CommandeFournisseur u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE) AND MONTH(u.createDate) = MONTH(CURRENT_DATE )")
    int countCommandeFournisseursByThisMonthAndYear();

    /**
     *cette année
     * @return
     */
    @Query("SELECT COALESCE(COUNT(u),0) FROM CommandeFournisseur u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE)")
    int countCommandeFournisseursByYear();

    /**
     * année président
     * @return
     */
    @Query("SELECT COALESCE(COUNT(u),0) FROM CommandeFournisseur u WHERE YEAR(u.createDate) = (YEAR(CURRENT_DATE)-1)")
    int countCommandeFournisseursByLastYear();


    /**
     * aujour'huit
     * @return
     */
    @Query("SELECT COALESCE(COUNT(u),0) FROM CommandeFournisseur u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE) AND DATE(u.createDate) = CURRENT_DATE")
    int countCommandeFournisseursByDay();

    /**
     * hier
     * @return
     */

    @Query("SELECT COALESCE(COUNT(u),0) FROM CommandeFournisseur u WHERE  YEAR(u.createDate) = YEAR(CURRENT_DATE) AND DATE(u.createDate) = :yesterdayDate")
    int countCommandeFournisseursByYesterday(@Param("yesterdayDate") Timestamp yesterdayDate);


    /********************************************* revenue ********************************************************************************************************/

    /**
     * mois président
     * @return
     */
    @Query("SELECT COALESCE(SUM(u.totalPrix),0)  FROM CommandeFournisseur u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE) AND MONTH(u.createDate) = (MONTH(CURRENT_DATE )-1)")
    int sumCommandeFournisseursByLastMonthAndYear();


    /**
     * mois actuel
     * @return
     */
    @Query("SELECT COALESCE(SUM(u.totalPrix),0)  FROM CommandeFournisseur u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE) AND MONTH(u.createDate) = MONTH(CURRENT_DATE )")
    int sumCommandeFournisseursByMonthAndYear();

    /**
     *cette année
     * @return
     */
    @Query("SELECT COALESCE(SUM(u.totalPrix),0) FROM CommandeFournisseur u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE)")
    int sumCommandeFournisseursByYear();

    /**
     * année président
     * @return
     */
    @Query("SELECT COALESCE(SUM(u.totalPrix),0) FROM CommandeFournisseur u WHERE   YEAR(u.createDate) = (YEAR(CURRENT_DATE)-1)")
    int sumCommandeFournisseursByLastYear();


    /**
     * aujour'huit
     * @return
     */
    @Query("SELECT COALESCE(SUM(u.totalPrix),0) FROM CommandeFournisseur u WHERE  DATE(u.createDate) = CURRENT_DATE")
    int sumCommandeFournisseursByDay();

    /**
     * hier
     * @return
     */

    @Query("SELECT COALESCE(SUM(u.totalPrix),0)  FROM CommandeFournisseur u WHERE   DATE(u.createDate) = :yesterdayDate")
    int sumCommandeFournisseursByYesterday(@Param("yesterdayDate") Timestamp yesterdayDate);


/***************************************** CMASEMENT DES COMMANDE PAR ORDER DESC DE TOTALPRIX ***************************************************/

    /**
     *Les commande fournisseur par order dec pour le mois actuel
     * @return
     */
    @Query("SELECT c FROM CommandeFournisseur c WHERE YEAR(c.createDate) = YEAR(CURRENT_DATE) AND MONTH(c.createDate) = MONTH(CURRENT_DATE )   ORDER BY c.totalPrix DESC LIMIT 5")
    List<CommandeFournisseur> findCmdFrsByMonthByOrderByTotalPrixDesc();


    /**
     *Les commande fournisseur par order dec pour le mois president
     * @return
     */
    @Query("SELECT c FROM CommandeFournisseur c WHERE YEAR(c.createDate) = YEAR(CURRENT_DATE) AND MONTH(c.createDate) = (MONTH(CURRENT_DATE )-1)   ORDER BY c.totalPrix DESC LIMIT 5")
    List<CommandeFournisseur> findCmdFrsByLastMonthByOrderByTotalPrixDesc();

    /**
     *Les commande fournisseur par order dec pour l'année actuel
     * @return
     */
    @Query("SELECT c FROM CommandeFournisseur c WHERE YEAR(c.createDate) = YEAR(CURRENT_DATE)   ORDER BY c.totalPrix DESC LIMIT 5")
    List<CommandeFournisseur> findCmdFrsByYearByOrderByTotalPrixDesc();

    /**
     *Les commande fournisseur par order dec pour l'année president
     * @return
     */
    @Query("SELECT c FROM CommandeFournisseur c WHERE YEAR(c.createDate) = (YEAR(CURRENT_DATE)-1)   ORDER BY c.totalPrix DESC LIMIT 5")
    List<CommandeFournisseur> findCmdFrsByLastYearByOrderByTotalPrixDesc();

    /**
     *Les commande fournisseur par order dec pour ajourd'huit
     * @return
     */
    @Query("SELECT c FROM CommandeFournisseur c WHERE YEAR(c.createDate) = YEAR(CURRENT_DATE) AND  DATE(c.createDate) = CURRENT_DATE  ORDER BY c.totalPrix DESC LIMIT 5")
    List<CommandeFournisseur> findCmdFrsByDayByOrderByTotalPrixDesc();


    /**
     *Les commande fournisseur par order dec pour hier
     * @return
     */
    @Query("SELECT c FROM CommandeFournisseur c WHERE YEAR(c.createDate) = YEAR(CURRENT_DATE) AND  DATE(c.createDate) = :yesterdayDate  ORDER BY c.totalPrix DESC LIMIT 5")
    List<CommandeFournisseur> findCmdFrsByLastDayByOrderByTotalPrixDesc(@Param("yesterdayDate") Timestamp yesterdayDate);
}
