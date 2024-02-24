package com.tahiri.gestiondestock.service;

import com.tahiri.gestiondestock.exception.EntityNotFoundException;
import com.tahiri.gestiondestock.exception.ErrorCodes;
import com.tahiri.gestiondestock.exception.InvalidEntityException;
import com.tahiri.gestiondestock.exception.InvalidOperationException;
import com.tahiri.gestiondestock.model.Article;
import com.tahiri.gestiondestock.model.Categorie;
import com.tahiri.gestiondestock.repository.ArticleRepository;
import com.tahiri.gestiondestock.repository.CategorieRepository;
import com.tahiri.gestiondestock.validator.CategorieValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Slf4j
public class CategorieService {
    @Autowired
    private CategorieRepository categorieRepository;
    @Autowired
    private ArticleRepository articleRepository;


    public Categorie save(Categorie categorie) {
        List<String> errors = CategorieValidator.validate(categorie);
        if (!errors.isEmpty()) {
            log.error("Article is not valid {}", categorie);
            throw new InvalidEntityException("La category n'est pas valide", ErrorCodes.CATEGORY_NOT_VALID, errors);
        }
        return categorieRepository.save(categorie);
    }

    public List<Categorie> getAll() {
        return categorieRepository.findAll();
    }

    public Categorie getById(Integer id) {
        if (id == null) {
            log.error("Category ID is null");
            return null;
        }
        return categorieRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucune category avec l'ID = " + id + " n' ete trouve dans la BDD",
                        ErrorCodes.CATEGORY_NOT_FOUND)
        );

    }

    public Categorie findByCode(String code) {

        if (!StringUtils.hasLength(code)) {
            log.error("Category CODE is null");
            return null;
        }

        return categorieRepository.findByCode(code).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucune categorie avec le CODE = " + code + " n' ete trouve dans la BDD",
                        ErrorCodes.CATEGORY_NOT_FOUND)
        );

    }

    public void delete(Integer id) {

        if (id == null) {
            log.error("Category ID is null");
            return;
        }

        List<Article> articles = articleRepository.findArticleByCategorie_Id(id);
        if (!articles.isEmpty()) {
            throw new InvalidOperationException("Impossible de supprimer cette categorie qui est deja utilise",
                    ErrorCodes.CATEGORY_ALREADY_IN_USE);
        }

        categorieRepository.deleteById(id);
    }


}

