package com.tahiri.gestiondestock.repository;


import com.tahiri.gestiondestock.model.Utilisateur;
import org.slf4j.MDC;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;


@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {



    Utilisateur findByEmailIgnoreCase(String email);


    @Transactional
    @Modifying
    @Query("update Utilisateur u set u.token = ?1 where u.id = ?2")
    int updateTokenById(String token, Integer id);

    long countByTokenLike(String token);

    List<Utilisateur> findByToken(String token);

  Optional < Utilisateur> findByEmail(String email);
    Page<Utilisateur> findByNomContainingAndEntrepriseId(String nom,Integer identreprise, Pageable pageable);


    /**
     * mois président
     * @return
     */
    @Query("SELECT COALESCE(COUNT(u),0) FROM Utilisateur u JOIN u.entreprise e  WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE) AND MONTH(u.createDate) = (MONTH(CURRENT_DATE )-1) AND e.id = :identreprise")
    int countUtilisateursByMonthAndYear(@Param("identreprise") String identreprise);


    /**
     * mois actuel
     * @return
     */
    @Query("SELECT COALESCE(COUNT(u),0) FROM Utilisateur u JOIN u.entreprise e WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE) AND MONTH(u.createDate) = MONTH(CURRENT_DATE ) AND e.id = :identreprise")
    int countUtilisateursByThisMonthAndYear(@Param("identreprise") String identreprise);

    /**
     *cette année
     * @return
     */
    @Query("SELECT COALESCE(COUNT(u),0) FROM Utilisateur u JOIN u.entreprise e WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE) AND e.id = :identreprise")
    int countUtilisateursByYear(@Param("identreprise") String identreprise);

    /**
     * année président
     * @return
     */
    @Query("SELECT COALESCE(COUNT(u),0) FROM Utilisateur u JOIN u.entreprise e WHERE YEAR(u.createDate) = (YEAR(CURRENT_DATE)-1) AND e.id = :identreprise")
    int countUtilisateursByLastYear(@Param("identreprise") String identreprise);


    /**
     * aujour'huit
     * @return
     */
    @Query("SELECT COALESCE(COUNT(u),0) FROM Utilisateur u JOIN u.entreprise e WHERE DATE(u.createDate) = CURRENT_DATE AND e.id = :identreprise")
    int countUtilisateursByDay(@Param("identreprise") String identreprise);

    /**
     * hier
     * @return
     */

    @Query("SELECT COALESCE(COUNT(u),0) FROM Utilisateur u JOIN u.entreprise e WHERE DATE(u.createDate) = :yesterdayDate AND e.id = :identreprise")
    int countUtilisateursByYesterday(@Param("yesterdayDate")  Timestamp  yesterdayDate,@Param("identreprise") String identreprise);

}
