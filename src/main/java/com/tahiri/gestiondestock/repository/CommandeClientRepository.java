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

    Page<CommandeClient> findByReferenceContainingAndIdEntreprise(String nom,Integer identreprise,  Pageable pageable);



/*********************************************************** nombre de commande **********************************************************************************/



    /**
     * mois président
     * @return
     */
    @Query("SELECT COALESCE(COUNT(u),0) FROM CommandeClient u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE) AND MONTH(u.createDate) = (MONTH(CURRENT_DATE )-1) AND u.idEntreprise = :identreprise")
    int countCommandeClientsByMonthAndYear(@Param("identreprise") String identreprise);


    /**
     * mois actuel
     * @return
     */
    @Query("SELECT COALESCE(COUNT(u),0) FROM CommandeClient u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE) AND MONTH(u.createDate) = MONTH(CURRENT_DATE ) AND u.idEntreprise = :identreprise")
    int countCommandeClientsByThisMonthAndYear(@Param("identreprise") String identreprise);

    /**
     *cette année
     * @return
     */
    @Query("SELECT COALESCE(COUNT(u),0) FROM CommandeClient u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE) AND u.idEntreprise = :identreprise")
    int countCommandeClientsByYear(@Param("identreprise") String identreprise);

    /**
     * année président
     * @return
     */
    @Query("SELECT COALESCE(COUNT(u),0) FROM CommandeClient u WHERE YEAR(u.createDate) = (YEAR(CURRENT_DATE)-1) AND u.idEntreprise = :identreprise")
    int countCommandeClientsByLastYear(@Param("identreprise") String identreprise);


    /**
     * aujour'huit
     * @return
     */
    @Query("SELECT COALESCE(COUNT(u),0) FROM CommandeClient u WHERE  YEAR(u.createDate) = YEAR(CURRENT_DATE) AND   DATE(u.createDate) = CURRENT_DATE AND u.idEntreprise = :identreprise")
    int countCommandeClientsByDay(@Param("identreprise") String identreprise);

    /**
     * hier
     * @return
     */

    @Query("SELECT COALESCE(COUNT(u),0) FROM CommandeClient u WHERE  u.idEntreprise = :identreprise AND  YEAR(u.createDate) = YEAR(CURRENT_DATE) AND  DATE(u.createDate) = :yesterdayDate  ")
    int countCommandeClientsByYesterday(@Param("yesterdayDate") Timestamp yesterdayDate,@Param("identreprise") String identreprise);




    /*************************************************   REVENUE   *************************************************************************************************/





    /**
     * revenue mois président
     * @return
     */
    @Query("SELECT COALESCE(SUM(u.totalPrix),0) FROM CommandeClient u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE) AND MONTH(u.createDate) = (MONTH(CURRENT_DATE )-1) AND u.idEntreprise = :identreprise")
    int sumCommandeClientsByLastMonthAndYear(@Param("identreprise") String identreprise);


    /**
     * revenue mois actuel
     * @return
     */
    @Query("SELECT COALESCE(SUM(u.totalPrix),0) FROM CommandeClient u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE) AND MONTH(u.createDate) = MONTH(CURRENT_DATE ) AND u.idEntreprise = :identreprise")
    int sumCommandeClientsByMonthAndYear(@Param("identreprise") String identreprise);

    /**
     *revenue cette année
     * @return
     */
    @Query("SELECT COALESCE(SUM(u.totalPrix),0) FROM CommandeClient u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE) AND u.idEntreprise = :identreprise")
    int sumCommandeClientsByYear(@Param("identreprise") String identreprise);

    /**
     * revenue année président
     * @return
     */
    @Query("SELECT COALESCE(SUM(u.totalPrix),0) FROM CommandeClient u WHERE YEAR(u.createDate) = (YEAR(CURRENT_DATE)-1) AND u.idEntreprise = :identreprise")
    int sumCommandeClientsByLastYear(@Param("identreprise") String identreprise);


    /**
     * revenue aujour'huit
     * @return
     */
    @Query("SELECT COALESCE(SUM(u.totalPrix),0) FROM CommandeClient u WHERE  YEAR(u.createDate) = YEAR(CURRENT_DATE) AND  DATE(u.createDate) = CURRENT_DATE AND u.idEntreprise = :identreprise")
    int sumCommandeClientsByDay(@Param("identreprise") String identreprise);

    /**
     *revenue hier
     * @return
     */

    @Query("SELECT COALESCE(SUM(u.totalPrix),0) FROM CommandeClient u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE) AND  DATE(u.createDate) = :yesterdayDate AND u.idEntreprise = :identreprise")
    int sumCommandeClientsByYesterday(@Param("yesterdayDate") Timestamp yesterdayDate,@Param("identreprise") String identreprise);

    @Query("SELECT c.etatCommande FROM CommandeClient c WHERE c.id = :id")
    String findEtatCommandeById(@Param("id") Integer id);

    /******************************************* CLASEMENT DES COMMANDE PAR ORDER DESC DE TOTALPRIX *******************************************************************/


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
    @Query("SELECT c FROM CommandeClient c WHERE YEAR(c.createDate) = YEAR(CURRENT_DATE) AND MONTH(c.createDate) = (MONTH(CURRENT_DATE )-1) AND c.idEntreprise = :identreprise  ORDER BY c.totalPrix DESC LIMIT 5")
    List<CommandeClient> findCmdCltByLastMonthByOrderByTotalPrixDesc(@Param("identreprise") String identreprise);

    /**
     *Les commande client par order dec pour l'année actuel
     * @return
     */
    @Query("SELECT c FROM CommandeClient c WHERE YEAR(c.createDate) = YEAR(CURRENT_DATE) AND c.idEntreprise = :identreprise   ORDER BY c.totalPrix DESC LIMIT 5")
    List<CommandeClient> findCmdCltByYearByOrderByTotalPrixDesc(@Param("identreprise") String identreprise);

    /**
     *Les commande client par order dec pour l'année president
     * @return
     */
    @Query("SELECT c FROM CommandeClient c WHERE YEAR(c.createDate) = (YEAR(CURRENT_DATE)-1) AND c.idEntreprise = :identreprise  ORDER BY c.totalPrix DESC LIMIT 5")
    List<CommandeClient> findCmdCltByLastYearByOrderByTotalPrixDesc(@Param("identreprise") String identreprise);

    /**
     *Les commande client par order dec pour ajourd'huit
     * @return
     */
    @Query("SELECT c FROM CommandeClient c WHERE YEAR(c.createDate) = YEAR(CURRENT_DATE) AND  DATE(c.createDate) = CURRENT_DATE  AND c.idEntreprise = :identreprise ORDER BY c.totalPrix DESC LIMIT 5")
    List<CommandeClient> findCmdCltByDayByOrderByTotalPrixDesc(@Param("identreprise") String identreprise);


    /**
     *Les commande client par order dec pour hier
     * @return
     */

    @Query("SELECT c FROM CommandeClient c WHERE YEAR(c.createDate) = YEAR(CURRENT_DATE) AND  DATE(c.createDate) = :yesterdayDate AND c.idEntreprise = :identreprise  ORDER BY c.totalPrix DESC LIMIT 5")
    List<CommandeClient> findCmdCltByLastDayByOrderByTotalPrixDesc(@Param("yesterdayDate") Timestamp yesterdayDate,@Param("identreprise") String identreprise);


}
