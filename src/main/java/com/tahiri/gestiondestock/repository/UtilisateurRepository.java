package com.tahiri.gestiondestock.repository;


import com.tahiri.gestiondestock.model.Utilisateur;
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
    Page<Utilisateur> findByNomContaining(String nom, Pageable pageable);

    /**
     * mois président
     * @return
     */
    @Query("SELECT COALESCE(COUNT(u),0) FROM Utilisateur u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE) AND MONTH(u.createDate) = (MONTH(CURRENT_DATE )-1)")
    int countUtilisateursByMonthAndYear();


    /**
     * mois actuel
     * @return
     */
    @Query("SELECT COALESCE(COUNT(u),0) FROM Utilisateur u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE) AND MONTH(u.createDate) = MONTH(CURRENT_DATE )")
    int countUtilisateursByThisMonthAndYear();

    /**
     *cette année
     * @return
     */
    @Query("SELECT COALESCE(COUNT(u),0) FROM Utilisateur u WHERE YEAR(u.createDate) = YEAR(CURRENT_DATE)")
    int countUtilisateursByYear();

    /**
     * année président
     * @return
     */
    @Query("SELECT COALESCE(COUNT(u),0) FROM Utilisateur u WHERE YEAR(u.createDate) = (YEAR(CURRENT_DATE)-1)")
    int countUtilisateursByLastYear();


    /**
     * aujour'huit
     * @return
     */
    @Query("SELECT COALESCE(COUNT(u),0) FROM Utilisateur u WHERE DATE(u.createDate) = CURRENT_DATE")
    int countUtilisateursByDay();

    /**
     * hier
     * @return
     */

    @Query("SELECT COALESCE(COUNT(u),0) FROM Utilisateur u WHERE DATE(u.createDate) = :yesterdayDate")
    int countUtilisateursByYesterday(@Param("yesterdayDate")  Timestamp  yesterdayDate);

}
