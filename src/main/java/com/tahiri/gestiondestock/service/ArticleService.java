package com.tahiri.gestiondestock.service;


import com.tahiri.gestiondestock.exception.EntityNotFoundException;
import com.tahiri.gestiondestock.exception.ErrorCodes;
import com.tahiri.gestiondestock.exception.InvalidEntityException;
import com.tahiri.gestiondestock.exception.InvalidOperationException;
import com.tahiri.gestiondestock.model.Article;
import com.tahiri.gestiondestock.model.LigneCommadeClient;
import com.tahiri.gestiondestock.model.LigneCommandeFournisseur;
import com.tahiri.gestiondestock.model.LigneVente;
import com.tahiri.gestiondestock.repository.ArticleRepository;
import com.tahiri.gestiondestock.repository.LigneCommandeClientRepository;
import com.tahiri.gestiondestock.repository.LigneCommandeFournisseurRepository;
import com.tahiri.gestiondestock.repository.LigneVenteRepository;
import com.tahiri.gestiondestock.validator.ArticleValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Slf4j
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private LigneVenteRepository ligneVenteRepository;

    @Autowired
    private LigneCommandeClientRepository ligneCommandeClientRepository;

    @Autowired
    private LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository;



    public Article save( Article article){
        List<String> errors = ArticleValidator.validate(article);
        if (!errors.isEmpty()) {
            log.error("Article is not valid {}", article);
            throw new InvalidEntityException("L'article n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID, errors);
        }
        return  articleRepository.save(article);


    }


    public  List<Article> getAll(){
        return articleRepository.findAll();
    }


    public  Article getById( Integer id){
        if (id == null) {
            log.error("Article ID is null");
            return null;
        }
        return  articleRepository.findById(id).orElseThrow(()->
                new EntityNotFoundException(
                        "Aucun article avec l'ID = " + id + " n' ete trouve dans la BDD",
                        ErrorCodes.ARTICLE_NOT_FOUND)
                );
    }
 public  Article finByCodeArticle(String  codeArticle){
     if (!StringUtils.hasLength(codeArticle)) {
         log.error("Article CODE is null");
         return null;
     }
     return articleRepository.findByCodeArticle(codeArticle).orElseThrow(()->
                     new EntityNotFoundException(
                             "Aucun article avec le CODE = " + codeArticle + " n' ete trouve dans la BDD",
                             ErrorCodes.ARTICLE_NOT_FOUND)
             );
 }

 public  List<LigneVente> findHistoriqueVentes(Integer idArticle){
        return ligneVenteRepository.findLigneVenteByArticle_Id(idArticle);
 }

public  List<LigneCommadeClient> findHistoriqueCommandeClient(Integer idArticle){
        return ligneCommandeClientRepository.findLigneCommadeClientByArticle_Id(idArticle);
}

 public  List<LigneCommandeFournisseur> findHistoriqurCommandeFournisseur(Integer idArticle){

       return ligneCommandeFournisseurRepository.findByArticle_Id(idArticle);
 }


 public  List<Article> findArticleByIdCategorie( Integer idCategorie){

        return articleRepository.findByCategorie_Id(idCategorie);
 }

    public void  delete(Integer id){

        if (id == null) {
            log.error("Article ID is null");
            return;
        }
     List<LigneCommadeClient> ligneCommadeClients= ligneCommandeClientRepository.findByArticle_Id(id);
        if (!ligneCommadeClients.isEmpty()) {
            throw new InvalidOperationException("Impossible de supprimer un article deja utilise dans des commandes client", ErrorCodes.ARTICLE_ALREADY_IN_USE);
        }

        List<LigneVente> ligneVentes = ligneVenteRepository.findByArticle_Id(id);
        if (!ligneVentes.isEmpty()) {
            throw new InvalidOperationException("Impossible de supprimer un article deja utilise dans des ventes",
                    ErrorCodes.ARTICLE_ALREADY_IN_USE);
        }
        articleRepository.deleteById(id);
    }
}
