package com.tahiri.gestiondestock.repository;

import com.tahiri.gestiondestock.model.CommandeClient;
import com.tahiri.gestiondestock.model.CommandeClientStats;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;


@Repository
public interface CommandeClientRepository extends JpaRepository<CommandeClient, Integer> {


    Optional<CommandeClient> findByReference(String reference);

    List<CommandeClient> findByClient_Id(Integer id);

    Page<CommandeClient> findByReferenceContaining(String nom, Pageable pageable);



/*********************************************************** nombre de commande **********************************************************************************/



    /**
     * mois président
     * @return
     */
    @Query("SELECT COALESCE(COUNT(u),0) FROM CommandeClient u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE) AND MONTH(u.createDate) = (MONTH(CURRENT_DATE )-1)")
    int countCommandeClientsByMonthAndYear();


    /**
     * mois actuel
     * @return
     */
    @Query("SELECT COALESCE(COUNT(u),0) FROM CommandeClient u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE) AND MONTH(u.createDate) = MONTH(CURRENT_DATE )")
    int countCommandeClientsByThisMonthAndYear();

    /**
     *cette année
     * @return
     */
    @Query("SELECT COALESCE(COUNT(u),0) FROM CommandeClient u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE)")
    int countCommandeClientsByYear();

    /**
     * année président
     * @return
     */
    @Query("SELECT COALESCE(COUNT(u),0) FROM CommandeClient u WHERE YEAR(u.createDate) = (YEAR(CURRENT_DATE)-1)")
    int countCommandeClientsByLastYear();


    /**
     * aujour'huit
     * @return
     */
    @Query("SELECT COALESCE(COUNT(u),0) FROM CommandeClient u WHERE  YEAR(u.createDate) = YEAR(CURRENT_DATE) AND   DATE(u.createDate) = CURRENT_DATE")
    int countCommandeClientsByDay();

    /**
     * hier
     * @return
     */

    @Query("SELECT COALESCE(COUNT(u),0) FROM CommandeClient u WHERE   YEAR(u.createDate) = YEAR(CURRENT_DATE) AND  DATE(u.createDate) = :yesterdayDate")
    int countCommandeClientsByYesterday(@Param("yesterdayDate") Timestamp yesterdayDate);




    /*************************************************   REVENUE   *************************************************************************************************/





    /**
     * revenue mois président
     * @return
     */
    @Query("SELECT COALESCE(SUM(u.totalPrix),0) FROM CommandeClient u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE) AND MONTH(u.createDate) = (MONTH(CURRENT_DATE )-1)")
    int sumCommandeClientsByLastMonthAndYear();


    /**
     * revenue mois actuel
     * @return
     */
    @Query("SELECT COALESCE(SUM(u.totalPrix),0) FROM CommandeClient u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE) AND MONTH(u.createDate) = MONTH(CURRENT_DATE )")
    int sumCommandeClientsByMonthAndYear();

    /**
     *revenue cette année
     * @return
     */
    @Query("SELECT COALESCE(SUM(u.totalPrix),0) FROM CommandeClient u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE)")
    int sumCommandeClientsByYear();

    /**
     * revenue année président
     * @return
     */
    @Query("SELECT COALESCE(SUM(u.totalPrix),0) FROM CommandeClient u WHERE YEAR(u.createDate) = (YEAR(CURRENT_DATE)-1)")
    int sumCommandeClientsByLastYear();


    /**
     * revenue aujour'huit
     * @return
     */
    @Query("SELECT COALESCE(SUM(u.totalPrix),0) FROM CommandeClient u WHERE  YEAR(u.createDate) = YEAR(CURRENT_DATE) AND  DATE(u.createDate) = CURRENT_DATE")
    int sumCommandeClientsByDay();

    /**
     *revenue hier
     * @return
     */

    @Query("SELECT COALESCE(SUM(u.totalPrix),0) FROM CommandeClient u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE) AND  DATE(u.createDate) = :yesterdayDate")
    int sumCommandeClientsByYesterday(@Param("yesterdayDate") Timestamp yesterdayDate);



    /******************************************* CMASEMENT DES COMMANDE PAR ORDER DESC DE TOTALPRIX *******************************************************************/


    /**
     *Les commande client par order dec pour le mois actuel
     * @return
     */
    /*
    @Query("SELECT c.id AS idcommande  , c.client.id AS idclient  , c.etatCommande AS status, c.totalPrix AS total,GROUP_CONCAT(l.article.id AS idarticle )  FROM CommandeClient c JOIN ligneCommandeClient l  ON c.id = l.idcommandeClient  WHERE YEAR(c.createDate) = YEAR(CURRENT_DATE) AND MONTH(c.createDate) = MONTH(CURRENT_DATE )  GROUP BY c.id   ORDER BY c.totalPrix DESC LIMIT 5")
    List<CommandeClientStats> findCmdCltByMonthByOrderByTotalPrixDescAndArticle();

*/

    /**
     *Les commande client par order dec pour le mois president
     * @return
     */
    @Query("SELECT c FROM CommandeClient c WHERE YEAR(c.createDate) = YEAR(CURRENT_DATE) AND MONTH(c.createDate) = (MONTH(CURRENT_DATE )-1)   ORDER BY c.totalPrix DESC LIMIT 5")
    List<CommandeClient> findCmdCltByLastMonthByOrderByTotalPrixDesc();

    /**
     *Les commande client par order dec pour l'année actuel
     * @return
     */
    @Query("SELECT c FROM CommandeClient c WHERE YEAR(c.createDate) = YEAR(CURRENT_DATE)   ORDER BY c.totalPrix DESC LIMIT 5")
    List<CommandeClient> findCmdCltByYearByOrderByTotalPrixDesc();

    /**
     *Les commande client par order dec pour l'année president
     * @return
     */
    @Query("SELECT c FROM CommandeClient c WHERE YEAR(c.createDate) = (YEAR(CURRENT_DATE)-1)   ORDER BY c.totalPrix DESC LIMIT 5")
    List<CommandeClient> findCmdCltByLastYearByOrderByTotalPrixDesc();

    /**
     *Les commande client par order dec pour ajourd'huit
     * @return
     */
    @Query("SELECT c FROM CommandeClient c WHERE YEAR(c.createDate) = YEAR(CURRENT_DATE) AND  DATE(c.createDate) = CURRENT_DATE  ORDER BY c.totalPrix DESC LIMIT 5")
    List<CommandeClient> findCmdCltByDayByOrderByTotalPrixDesc();


    /**
     *Les commande client par order dec pour hier
     * @return
     */
    @Query("SELECT c FROM CommandeClient c WHERE YEAR(c.createDate) = YEAR(CURRENT_DATE) AND  DATE(c.createDate) = :yesterdayDate  ORDER BY c.totalPrix DESC LIMIT 5")
    List<CommandeClient> findCmdCltByLastDayByOrderByTotalPrixDesc(@Param("yesterdayDate") Timestamp yesterdayDate);
}
