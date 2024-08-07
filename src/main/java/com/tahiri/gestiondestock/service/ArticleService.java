package com.tahiri.gestiondestock.service;
import com.tahiri.gestiondestock.exception.EntityNotFoundException;
import com.tahiri.gestiondestock.exception.ErrorCodes;
import com.tahiri.gestiondestock.exception.InvalidEntityException;
import com.tahiri.gestiondestock.exception.InvalidOperationException;
import com.tahiri.gestiondestock.model.*;
import com.tahiri.gestiondestock.repository.ArticleRepository;
import com.tahiri.gestiondestock.repository.LigneCommandeClientRepository;
import com.tahiri.gestiondestock.repository.LigneCommandeFournisseurRepository;
import com.tahiri.gestiondestock.validator.ArticleValidator;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.springframework.data.domain.PageRequest.of;
@Service
@Slf4j
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private LigneCommandeClientRepository ligneCommandeClientRepository;
    @Autowired
    private LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository;
    /**
     * Service pour ajouter l'article à la base de données
     *
     * @param article
     * @return
     */
    public Article save(Article article) {
        List<String> errors = ArticleValidator.validate(article);
        if (!errors.isEmpty()) {
            log.error("Article is not valid {}", article);
            throw new InvalidEntityException("L'article n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID, errors);
        }
        return articleRepository.save(article);
    }
    /**
     * Service qui retourn tous les articles
     *
     * @return
     */
    public List<Article> getAll() {
        return articleRepository.findAll();
    }
    /**
     * Service qui retoun l'article par son id
     *
     * @param id
     * @return
     */
    public Article getById(Integer id) {
        if (id == null) {
            log.error("Article ID is null");
            return null;
        }
        return articleRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun article avec l'ID = " + id + " n' ete trouve dans la BDD",
                        ErrorCodes.ARTICLE_NOT_FOUND)
        );
    }
    /**
     * Service qui retourn les articles par son codeArticle
     *
     * @param codeArticle
     * @return
     */
    public Article finByCodeArticle(String codeArticle) {
        if (!StringUtils.hasLength(codeArticle)) {
            log.error("Article CODE is null");
            return null;
        }
        return articleRepository.findByCodeArticle(codeArticle).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun article avec le CODE = " + codeArticle + " n' ete trouve dans la BDD",
                        ErrorCodes.ARTICLE_NOT_FOUND)
        );
    }
    /**
     * Service qui retourn les LigneCommandeClient par id article
     *
     * @param idArticle
     * @return
     */
    public List<LigneCommandeClient> findHistoriqueCommandeClient(Integer idArticle) {
        return ligneCommandeClientRepository.findLigneCommandeClientByArticle_Id(idArticle);
    }
    /**
     * Service qui retourn les LigneCommandeFournisseur par id article
     *
     * @param idArticle
     * @return
     */
    public List<LigneCommandeFournisseur> findHistoriqurCommandeFournisseur(Integer idArticle) {

        return ligneCommandeFournisseurRepository.findByArticle_Id(idArticle);
    }
    /**
     * Service qui retourn les articles par id categorie
     *
     * @param idCategorie
     * @return
     */
    public List<Article> findArticleByIdCategorie(Integer idCategorie) {

        return articleRepository.findByCategorie_Id(idCategorie);
    }
    /**
     * Service pour supprimer un article par son id
     *
     * @param id
     */
    public void delete(Integer id) {

        if (id == null) {
            log.error("Article ID is null");
            return;
        }
        List<LigneCommandeClient> ligneCommandeClients = ligneCommandeClientRepository.findByArticle_Id(id);
        if (!ligneCommandeClients.isEmpty()) {
            throw new InvalidOperationException("Impossible de supprimer un article deja utilise dans des commandes client", ErrorCodes.ARTICLE_ALREADY_IN_USE);
        }

        articleRepository.deleteById(id);
    }
    /**
     * Service pour recupirer les articles par page et par recherche
     *
     * @param name
     * @param page
     * @param size
     * @return
     */
    public Page<Article> getArticles(String name, int page, int size) {
        String identreprise = MDC.get("idEntreprise");
        return articleRepository.findByDesignationContainingAndIdEntreprise(name, Integer.valueOf(identreprise), of(page, size));
    }
}
