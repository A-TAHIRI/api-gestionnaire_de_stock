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

    Page<CommandeFournisseur> findByReferenceContainingAndIdEntreprise(String nom,Integer identreprise, Pageable pageable);

    @Query("SELECT f.etatCommande FROM CommandeFournisseur f WHERE f.id = :id")
    String findEtatCommandeById(@Param("id") Integer id);

/***************************************** nombre de commande *****************************************************************************************/

    /**
     * mois président
     * @return
     */
    @Query("SELECT COALESCE(COUNT(u),0) FROM CommandeFournisseur u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE) AND MONTH(u.createDate) = (MONTH(CURRENT_DATE )-1) AND u.idEntreprise = :identreprise")
    int countCommandeFournisseursByMonthAndYear(@Param("identreprise") String identreprise);


    /**
     * mois actuel
     * @return
     */
    @Query("SELECT COALESCE(COUNT(u),0) FROM CommandeFournisseur u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE) AND MONTH(u.createDate) = MONTH(CURRENT_DATE ) AND u.idEntreprise = :identreprise")
    int countCommandeFournisseursByThisMonthAndYear(@Param("identreprise") String identreprise);

    /**
     *cette année
     * @return
     */
    @Query("SELECT COALESCE(COUNT(u),0) FROM CommandeFournisseur u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE) AND u.idEntreprise = :identreprise")
    int countCommandeFournisseursByYear(@Param("identreprise") String identreprise);

    /**
     * année président
     * @return
     */
    @Query("SELECT COALESCE(COUNT(u),0) FROM CommandeFournisseur u WHERE YEAR(u.createDate) = (YEAR(CURRENT_DATE)-1) AND u.idEntreprise = :identreprise")
    int countCommandeFournisseursByLastYear(@Param("identreprise") String identreprise);


    /**
     * aujour'huit
     * @return
     */
    @Query("SELECT COALESCE(COUNT(u),0) FROM CommandeFournisseur u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE) AND DATE(u.createDate) = CURRENT_DATE AND u.idEntreprise = :identreprise")
    int countCommandeFournisseursByDay(@Param("identreprise") String identreprise);

    /**
     * hier
     * @return
     */

    @Query("SELECT COALESCE(COUNT(u),0) FROM CommandeFournisseur u WHERE  YEAR(u.createDate) = YEAR(CURRENT_DATE) AND DATE(u.createDate) = :yesterdayDate AND u.idEntreprise = :identreprise")
    int countCommandeFournisseursByYesterday(@Param("yesterdayDate") Timestamp yesterdayDate,@Param("identreprise") String identreprise);


    /********************************************* DEPENCE ********************************************************************************************************/

    /**
     * mois président
     * @return
     */
    @Query("SELECT COALESCE(SUM(u.totalPrix),0)  FROM CommandeFournisseur u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE) AND MONTH(u.createDate) = (MONTH(CURRENT_DATE )-1) AND u.idEntreprise = :identreprise")
    int sumCommandeFournisseursByLastMonthAndYear(@Param("identreprise") String identreprise);


    /**
     * mois actuel
     * @return
     */
    @Query("SELECT COALESCE(SUM(u.totalPrix),0)  FROM CommandeFournisseur u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE) AND MONTH(u.createDate) = MONTH(CURRENT_DATE ) AND u.idEntreprise = :identreprise")
    int sumCommandeFournisseursByMonthAndYear(@Param("identreprise") String identreprise);

    /**
     *cette année
     * @return
     */
    @Query("SELECT COALESCE(SUM(u.totalPrix),0) FROM CommandeFournisseur u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE) AND u.idEntreprise = :identreprise")
    int sumCommandeFournisseursByYear(@Param("identreprise") String identreprise);

    /**
     * année président
     * @return
     */
    @Query("SELECT COALESCE(SUM(u.totalPrix),0) FROM CommandeFournisseur u WHERE   YEAR(u.createDate) = (YEAR(CURRENT_DATE)-1) AND u.idEntreprise = :identreprise")
    int sumCommandeFournisseursByLastYear(@Param("identreprise") String identreprise);


    /**
     * aujour'huit
     * @return
     */
    @Query("SELECT COALESCE(SUM(u.totalPrix),0) FROM CommandeFournisseur u WHERE  DATE(u.createDate) = CURRENT_DATE AND u.idEntreprise = :identreprise")
    int sumCommandeFournisseursByDay(@Param("identreprise") String identreprise);

    /**
     * hier
     * @return
     */

    @Query("SELECT COALESCE(SUM(u.totalPrix),0)  FROM CommandeFournisseur u WHERE   DATE(u.createDate) = :yesterdayDate AND u.idEntreprise = :identreprise")
    int sumCommandeFournisseursByYesterday(@Param("yesterdayDate") Timestamp yesterdayDate,@Param("identreprise") String identreprise);


/***************************************** CLASEMENT DES COMMANDE PAR ORDER DESC DE TOTALPRIX ***************************************************/

    /**
     *Les commande fournisseur par order dec pour le mois actuel
     * @return
     */
    @Query("SELECT c FROM CommandeFournisseur c WHERE YEAR(c.createDate) = YEAR(CURRENT_DATE) AND MONTH(c.createDate) = MONTH(CURRENT_DATE ) AND c.idEntreprise = :identreprise  ORDER BY c.totalPrix DESC LIMIT 5")
    List<CommandeFournisseur> findCmdFrsByMonthByOrderByTotalPrixDesc(@Param("identreprise") String identreprise);


    /**
     *Les commande fournisseur par order dec pour le mois president
     * @return
     */
    @Query("SELECT c FROM CommandeFournisseur c WHERE YEAR(c.createDate) = YEAR(CURRENT_DATE) AND MONTH(c.createDate) = (MONTH(CURRENT_DATE )-1) AND c.idEntreprise = :identreprise  ORDER BY c.totalPrix DESC LIMIT 5")
    List<CommandeFournisseur> findCmdFrsByLastMonthByOrderByTotalPrixDesc(@Param("identreprise") String identreprise);

    /**
     *Les commande fournisseur par order dec pour l'année actuel
     * @return
     */
    @Query("SELECT c FROM CommandeFournisseur c WHERE YEAR(c.createDate) = YEAR(CURRENT_DATE) AND c.idEntreprise = :identreprise  ORDER BY c.totalPrix DESC LIMIT 5")
    List<CommandeFournisseur> findCmdFrsByYearByOrderByTotalPrixDesc(@Param("identreprise") String identreprise);

    /**
     *Les commande fournisseur par order dec pour l'année president
     * @return
     */
    @Query("SELECT c FROM CommandeFournisseur c WHERE YEAR(c.createDate) = (YEAR(CURRENT_DATE)-1)  AND c.idEntreprise = :identreprise  ORDER BY c.totalPrix DESC LIMIT 5")
    List<CommandeFournisseur> findCmdFrsByLastYearByOrderByTotalPrixDesc(@Param("identreprise") String identreprise);

    /**
     *Les commande fournisseur par order dec pour ajourd'huit
     * @return
     */
    @Query("SELECT c FROM CommandeFournisseur c WHERE YEAR(c.createDate) = YEAR(CURRENT_DATE) AND  DATE(c.createDate) = CURRENT_DATE AND c.idEntreprise = :identreprise  ORDER BY c.totalPrix DESC LIMIT 5")
    List<CommandeFournisseur> findCmdFrsByDayByOrderByTotalPrixDesc(@Param("identreprise") String identreprise);


    /**
     *Les commande fournisseur par order dec pour hier
     * @return
     */
    @Query("SELECT c FROM CommandeFournisseur c WHERE YEAR(c.createDate) = YEAR(CURRENT_DATE) AND  DATE(c.createDate) = :yesterdayDate  AND c.idEntreprise = :identreprise ORDER BY c.totalPrix DESC LIMIT 5")
    List<CommandeFournisseur> findCmdFrsByLastDayByOrderByTotalPrixDesc(@Param("yesterdayDate") Timestamp yesterdayDate,@Param("identreprise") String identreprise);
}
