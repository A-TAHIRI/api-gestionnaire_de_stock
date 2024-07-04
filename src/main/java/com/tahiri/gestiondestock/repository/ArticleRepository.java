package com.tahiri.gestiondestock.repository;


import com.tahiri.gestiondestock.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer > {

    Optional<Article> findByCodeArticle(String codeArticle);

    List<Article> findByCategorie_Id(Integer id);

    List<Article> findArticleByCategorie_Id(Integer id);

    Page<Article> findByDesignationContainingAndIdEntreprise(String name, Integer identreprise, Pageable pageable);
}
