package com.tahiri.gestiondestock.controller;
import com.tahiri.gestiondestock.dto.ArticleDto;
import com.tahiri.gestiondestock.dto.CategorieDto;
import com.tahiri.gestiondestock.exception.WsException;
import com.tahiri.gestiondestock.model.Article;
import com.tahiri.gestiondestock.model.Categorie;
import com.tahiri.gestiondestock.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tahiri.gestiondestock.utils.constant.ARTICLE_ENDPOINT;
@RestController
@RequestMapping(ARTICLE_ENDPOINT)
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    /**
     * method pour récupirer tous les articles
     * @return une liste des articles
     */
    @GetMapping("")
    public List<ArticleDto>  tousAricles(){
        List<Article> articles = articleService.getAll();
        List<ArticleDto> articleDtos = new ArrayList<>();
        for (Article a: articles){
            articleDtos.add(new ArticleDto(a));
        }
        return articleDtos;
    }
    /**
     * method pour recupirer un article
     * @param id
     * @return l'article de l'id
     */
    @GetMapping("/{id}")
    public  ArticleDto unArticle(@PathVariable Integer id){
        return  new ArticleDto(articleService.getById(id));

    }
    /**
     * method pour ajouter un article
     * @param article
     * @return
     */
    @PostMapping("")
    public  ArticleDto enregister(@RequestBody Article article ){
        return new ArticleDto(articleService.save(article));
    }
    /**
     * method pour supprimer l'article de l'id
     * @param id
     */
    @DeleteMapping("/{id}")
    public  void  supprimer(@PathVariable Integer id){
        articleService.delete(id);
    }
    /**
     * method pour modifier l'article de l'id
     * @param id
     * @param article
     * @return l'articl
     */
    @PutMapping("/{id}")
    public  ArticleDto modifier(@PathVariable Integer id , @RequestBody Article article){
        Article oldarticle =articleService.getById(id);
        if (oldarticle != null){
            article.setId(id);
            return  new ArticleDto(articleService.save(article));
        }else{
            return  new ArticleDto(article);
        }
    }
    /**
     * Methode qui retourn l'article par son code
     * @param codeArticle
     * @return
     */
    @GetMapping("/filter/{codeArticle}")
   public ArticleDto getArticleByCode(@PathVariable String codeArticle){
        return new ArticleDto(articleService.finByCodeArticle(codeArticle)) ;
    }
    /**
     * Methode qui retourn les article par page, et par recherche
     * @param name
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/articles")
    public Page<ArticleDto> getArticles(@RequestParam( defaultValue = "", required = false , name = "nom") String name,
                                            @RequestParam Optional<Integer> page,
                                            @RequestParam Optional<Integer> size){

        Page<Article>   articlePage  =   this.articleService.getArticles(name,page.orElse(0),size.orElse(10));

        Page<ArticleDto> articleDtoPage =articlePage.map(article -> {
            ArticleDto articleDto =new ArticleDto(article);
            return articleDto;
        });
        return articleDtoPage;

    }
}
