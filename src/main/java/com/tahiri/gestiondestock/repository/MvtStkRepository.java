package com.tahiri.gestiondestock.repository;


import com.tahiri.gestiondestock.model.MvtStk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MvtStkRepository  extends JpaRepository<MvtStk ,Integer> {

    @Query("select sum(m.quantite) from MvtStk m where m.article.id = :idArticle")
    BigDecimal stockReelArticle(@Param("idArticle") Integer idArticle);

    List<MvtStk> findByArticle_Id(Integer id);

    @Query("SELECT  m FROM MvtStk m GROUP BY m.article.id")
    List<MvtStk> findAllGroupByIdArticle();

    /*

    @Query("SELECT m FROM MvtStk m WHERE m.article.designation LIKE %:name% AND m.idEntreprise = :identreprise GROUP BY m.article.id, m.article.designation, m.createDate, m.lastModifiedDate, m.dateMvt,m.id, m.quantite, m.typeMvt")
    Page<MvtStk> findAllGroupByIdArticleContainingAndIdEntreprise(@Param("name") String name,@Param("identreprise") Integer identreprise,  Pageable pageable);
*/
/*
    @Query("SELECT m FROM MvtStk m WHERE m.id IN (SELECT MIN(m2.id) FROM MvtStk m2 WHERE m2.article.designation LIKE %:name% AND m2.idEntreprise = :identreprise GROUP BY m2.article.id)")
    Page<MvtStk> findAllGroupByIdArticleContainingAndIdEntreprise(@Param("name") String name, @Param("identreprise") Integer identreprise, Pageable pageable);
*/

    @Query("SELECT m FROM MvtStk m WHERE m.article.designation LIKE %:name% AND m.idEntreprise = :identreprise GROUP BY m.article.id, m.article.designation")
    Page<MvtStk> findAllGroupByIdArticleContainingAndIdEntreprise(@Param("name") String name,@Param("identreprise") Integer identreprise,  Pageable pageable);

    MvtStk findByIdLignefrsclt(Integer idLignefrsclt);
}

