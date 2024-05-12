package com.tahiri.gestiondestock.repository;

import com.tahiri.gestiondestock.model.ArticleStats;
import com.tahiri.gestiondestock.model.LigneCommandeClient;
import org.slf4j.MDC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LigneCommandeClientRepository extends JpaRepository<LigneCommandeClient, Integer> {
    List<LigneCommandeClient> findByArticle_Id(Integer id);

    List<LigneCommandeClient> findByCommandeClient_Id(Integer id);

    List<LigneCommandeClient> findLigneCommadeClientByArticle_Id(Integer idArticle);

    List<LigneCommandeClient> findLigneCommandeClientByArticle_Id(Integer idArticle);




    /**
     * récupirer les top 5 article les plus demmander aujourd'huit
     * @return
     */
    @Query("SELECT l.article.designation as designation , l.article.image as image, COALESCE(COUNT(l),0) as nbLigne, COALESCE(SUM(l.quantite),0) as sumQuantite, COALESCE(SUM(l.quantite * l.prixUnitaire),0) as total " +
            "FROM LigneCommandeClient l " +
            "WHERE YEAR(l.createDate) = YEAR(CURRENT_DATE) AND DATE(l.createDate) = CURRENT_DATE " +
            "AND l.idEntreprise = :identreprise " +
            "GROUP BY l.article.id " +
            "ORDER BY nbLigne DESC " +
            "LIMIT 5")
    List<ArticleStats> findTopArticlesByCommandesToDay(@Param("identreprise") String identreprise);



    /**
     * récupirer les top 5 article les plus demmander dans ce mois actuel
     * @return
     */
    @Query("SELECT l.article.designation as designation , l.article.image as image, COALESCE(COUNT(l),0) as nbLigne, COALESCE(SUM(l.quantite),0) as sumQuantite, COALESCE(SUM(l.quantite * l.prixUnitaire),0) as total " +
            "FROM LigneCommandeClient l " +
            "WHERE YEAR(l.createDate) = YEAR(CURRENT_DATE) AND MONTH(l.createDate) = MONTH(CURRENT_DATE ) " +
            "AND l.idEntreprise = :identreprise " +
            "GROUP BY l.article.id " +
            "ORDER BY nbLigne DESC " +
            "LIMIT 5")
    List<ArticleStats> findTopArticlesByCommandesToMonth(@Param("identreprise") String identreprise);


    /**
     * récupirer les top 5 article les plus demmander dans l'année actuel
     * @return
     */
    @Query("SELECT l.article.designation as designation , l.article.image as image, COALESCE(COUNT(l),0) as nbLigne, COALESCE(SUM(l.quantite),0) as sumQuantite, COALESCE(SUM(l.quantite * l.prixUnitaire),0) as total " +
            "FROM LigneCommandeClient l " +
            "WHERE  YEAR(l.createDate) = YEAR(CURRENT_DATE) "+
            "AND l.idEntreprise = :identreprise " +
            "GROUP BY l.article.id " +
            "ORDER BY nbLigne DESC " +
            "LIMIT 5")
    List<ArticleStats> findTopArticlesByCommandesToYear(@Param("identreprise") String identreprise);


}
