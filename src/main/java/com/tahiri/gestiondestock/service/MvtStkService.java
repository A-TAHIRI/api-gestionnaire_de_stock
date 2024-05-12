package com.tahiri.gestiondestock.service;


import com.tahiri.gestiondestock.exception.ErrorCodes;
import com.tahiri.gestiondestock.exception.InvalidEntityException;
import com.tahiri.gestiondestock.model.MvtStk;
import com.tahiri.gestiondestock.model.TypeMvtStk;
import com.tahiri.gestiondestock.model.Utilisateur;
import com.tahiri.gestiondestock.repository.MvtStkRepository;
import com.tahiri.gestiondestock.validator.MvtStkValidator;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.data.domain.PageRequest.of;

@Service
@Slf4j
public class MvtStkService {
    String identreprise = MDC.get("idEntreprise");
    @Autowired
    private MvtStkRepository mvtStkRepository;

    @Autowired
    private  ArticleService articleService;



    public BigDecimal stockReelArticle(Integer idArticle) {
        if (idArticle == null) {
            log.warn("ID article is NULL");
            return BigDecimal.valueOf(-1);
        }
        articleService.getById(idArticle);
        return mvtStkRepository.stockReelArticle(idArticle);
    }
    public MvtStk save(MvtStk mvtStk){
        return  this.mvtStkRepository.save(mvtStk);
    }

    public  List<MvtStk> mvtStkArticle(Integer idArticle){
        return mvtStkRepository.findByArticle_Id(idArticle);
    }

    public  List<MvtStk> mvtStkGrpouByArticle(){
        String identreprise = MDC.get("idEntreprise");
        return mvtStkRepository.findAllGroupByIdArticle( );
    }



    public MvtStk entreeStock(MvtStk mvtStk) {
        return entreePositive(mvtStk, TypeMvtStk.ENTREE);
    }
    public MvtStk sortieStock(MvtStk mvtStk) {
        return sortieNegative(mvtStk, TypeMvtStk.SORTIE);
    }
    public MvtStk correctionStockPos(MvtStk mvtStk) {
        return entreePositive(mvtStk, TypeMvtStk.CORRECTION_POS);
    }

    public MvtStk correctionStockNeg(MvtStk mvtStk) {
        return sortieNegative(mvtStk, TypeMvtStk.CORRECTION_NEG);
    }

    /**
     * Service qui retourn mvtstk par id de ligne de commande
     * @param id
     * @return
     */
    public MvtStk findByIdLigneCltFrs(Integer id ){
        return mvtStkRepository.findByIdLignefrsclt(id);
    }

    /**
     * Service pour suprimer mvtstk
     * @param mvtStk
     */
    public  void delet( MvtStk mvtStk){
        this.mvtStkRepository.delete(mvtStk);
    }

    private MvtStk entreePositive(MvtStk mvtStk, TypeMvtStk typeMvtStk) {
        List<String> errors = MvtStkValidator.validate(mvtStk);
        if (!errors.isEmpty()) {
            log.error("Article is not valid {}", mvtStk);
            throw new InvalidEntityException("Le mouvement du stock n'est pas valide", ErrorCodes.MVT_STK_NOT_VALID, errors);
        }
        mvtStk.setQuantite(
                BigDecimal.valueOf(
                        Math.abs ((long) mvtStk.getQuantite().doubleValue())
                )
        );
        mvtStk.setTypeMvt(typeMvtStk);
        return  mvtStkRepository.save(mvtStk);


    }
    private MvtStk sortieNegative(MvtStk mvtStk, TypeMvtStk typeMvtStk) {
        List<String> errors = MvtStkValidator.validate(mvtStk);
        if (!errors.isEmpty()) {
            log.error("Article is not valid {}", mvtStk);
            throw new InvalidEntityException("Le mouvement du stock n'est pas valide", ErrorCodes.MVT_STK_NOT_VALID, errors);
        }
        mvtStk.setQuantite(BigDecimal.valueOf(mvtStk.getQuantite().doubleValue() * -1));
        mvtStk.setTypeMvt(typeMvtStk);
        return mvtStkRepository.save(mvtStk);

    }

    public List<MvtStk> getAll() {
        return this.mvtStkRepository.findAll();
    }


    /**
     * Service pour recupirer les MvtStk par page et par recherche
     * @param nom
     * @param page
     * @param size
     * @return
     */
    public Page<MvtStk> getmvtstkbyarticle(String nom, int page, int size){
        String identreprise = MDC.get("idEntreprise");
        return mvtStkRepository.findAllGroupByIdArticleContainingAndIdEntreprise(nom,Integer.valueOf(identreprise), of(page,size));
    }
}
