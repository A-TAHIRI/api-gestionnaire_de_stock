package com.tahiri.gestiondestock.service;


import com.tahiri.gestiondestock.exception.ErrorCodes;
import com.tahiri.gestiondestock.exception.InvalidEntityException;
import com.tahiri.gestiondestock.model.MvtStk;
import com.tahiri.gestiondestock.model.TypeMvtStk;
import com.tahiri.gestiondestock.repository.MvtStkRepository;
import com.tahiri.gestiondestock.validator.MvtStkValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class MvtStkService {

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

    public  List<MvtStk> mvtStkArticle(Integer idArticle){
        return mvtStkRepository.findByArticle_Id(idArticle);
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
        mvtStk.setQuantite(BigDecimal.valueOf(  Math.abs(mvtStk.getQuantite().doubleValue() * -1) ));
        mvtStk.setTypeMvt(typeMvtStk);
        return mvtStkRepository.save(mvtStk);

    }
}
