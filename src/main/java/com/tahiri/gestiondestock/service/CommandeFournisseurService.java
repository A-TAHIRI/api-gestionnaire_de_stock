package com.tahiri.gestiondestock.service;

import com.tahiri.gestiondestock.exception.EntityNotFoundException;
import com.tahiri.gestiondestock.exception.ErrorCodes;
import com.tahiri.gestiondestock.exception.InvalidEntityException;
import com.tahiri.gestiondestock.exception.InvalidOperationException;
import com.tahiri.gestiondestock.model.*;
import com.tahiri.gestiondestock.repository.ArticleRepository;
import com.tahiri.gestiondestock.repository.CommandeFournisseurRepository;
import com.tahiri.gestiondestock.repository.FournisseurRepository;
import com.tahiri.gestiondestock.repository.LigneCommandeFournisseurRepository;
import com.tahiri.gestiondestock.validator.ArticleValidator;
import com.tahiri.gestiondestock.validator.CommandeFournisseurValidator;
import com.tahiri.gestiondestock.validator.LigneCommadeClientValidator;
import com.tahiri.gestiondestock.validator.LigneCommadeFournisseurValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.PageRequest.of;

@Service
@Slf4j
public class CommandeFournisseurService {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private CommandeFournisseurRepository commandeFournisseurRepository;
    @Autowired
    private LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository;
    @Autowired
    private FournisseurRepository fournisseurRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private MvtStkService mvtStkService;

    public CommandeFournisseur save(CommandeFournisseur commandeFournisseur) {

        List<String> errors = CommandeFournisseurValidator.validate(commandeFournisseur);




        if (!errors.isEmpty()) {
            log.error("Commande fournisseur n'est pas valide");
            throw new InvalidEntityException("La commande fournisseur n'est pas valide", ErrorCodes.COMMANDE_FOURNISSEUR_NOT_VALID, errors);
        }

        if (commandeFournisseur.getId() != null && commandeFournisseur.isCommandeLivree()) {
            throw new InvalidOperationException("Impossible de modifier la commande lorsqu'elle est livree", ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }

        Optional<Fournisseur> fournisseur = fournisseurRepository.findById(commandeFournisseur.getFournisseur().getId());
        if (fournisseur.isEmpty()) {
            log.warn("Fournisseur with ID {} was not found in the DB", commandeFournisseur.getFournisseur().getId());
            throw new EntityNotFoundException("Aucun fournisseur avec l'ID" + commandeFournisseur.getFournisseur().getId() + " n'a ete trouve dans la BDD",
                    ErrorCodes.FOURNISSEUR_NOT_FOUND);
        }

        List<String> articleErrors = new ArrayList<>();

        if (commandeFournisseur.getLigneCommandeFournisseurs() != null) {
            commandeFournisseur.getLigneCommandeFournisseurs().forEach(ligCmdFrs -> {
                if (ligCmdFrs.getArticle() != null) {
                    Optional<Article> article = articleRepository.findById(ligCmdFrs.getArticle().getId());
                    if (article.isEmpty()) {
                        commandeFournisseur.getLigneCommandeFournisseurs().forEach(ligne->{
                            LigneCommadeFournisseurValidator.validate(ligne).forEach(elm->{
                                errors.add(elm);
                            });
                        });
                        articleErrors.add("L'article avec l'ID " + ligCmdFrs.getArticle().getId() + " n'existe pas");
                    }

                } else {
                    commandeFournisseur.getLigneCommandeFournisseurs().forEach(ligne->{
                        LigneCommadeFournisseurValidator.validate(ligne).forEach(elm->{
                            errors.add(elm);
                        });
                    });
                    articleErrors.add("Impossible d'enregister une commande avec un aticle NULL");
                }
            });
        }



        if (!articleErrors.isEmpty()) {
            log.warn("");
            throw new InvalidEntityException("Article n'existe pas dans la BDD", ErrorCodes.ARTICLE_NOT_FOUND, articleErrors);
        }
        commandeFournisseur.setDateCommande(Instant.now());
        CommandeFournisseur savedCmdFrs = commandeFournisseurRepository.save(commandeFournisseur);

        if (commandeFournisseur.getLigneCommandeFournisseurs() != null) {
            commandeFournisseur.getLigneCommandeFournisseurs().forEach(ligCmdFrs -> {
                LigneCommandeFournisseur ligneCommandeFournisseur = ligneCommandeFournisseurRepository.save(ligCmdFrs);
                ligneCommandeFournisseur.setCommandeFournisseur(savedCmdFrs);
                ligneCommandeFournisseur.setIdEntreprise(savedCmdFrs.getIdEntreprise());
                LigneCommandeFournisseur saveLigne = ligneCommandeFournisseurRepository.save(ligneCommandeFournisseur);

                effectuerEntree(saveLigne);
            });
        }

        return commandeFournisseurRepository.save(savedCmdFrs);

    }

    public CommandeFournisseur getById(Integer id) {
        if (id == null) {
            log.error("Commande fournisseur ID is NULL");
            return null;
        }
        return commandeFournisseurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune commande fournisseur n'a ete trouve avec l'ID " + id, ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND
                ));
    }

    public CommandeFournisseur findByCode(String reference) {
        if (!StringUtils.hasLength(reference)) {
            log.error("Commande fournisseur CODE is NULL");
            return null;
        }
        return commandeFournisseurRepository.findByReference(reference)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune commande fournisseur n'a ete trouve avec le CODE " + reference, ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND
                ));
    }
    public List<CommandeFournisseur> getAll() {
        return commandeFournisseurRepository.findAll();

    }
    public List<LigneCommandeFournisseur> findAllLignesCommandesFournisseurByCommandeFournisseurId(Integer idCommande) {
        return ligneCommandeFournisseurRepository.findByCommandeFournisseur_Id(idCommande);

    }
    public void delete(Integer id) {
        if (id == null) {
            log.error("Commande fournisseur ID is NULL");
            return;
        }
        List<LigneCommandeFournisseur> ligneCommandeFournisseurs = ligneCommandeFournisseurRepository.findByCommandeFournisseur_Id(id);
        if (!ligneCommandeFournisseurs.isEmpty()) {
            throw new InvalidOperationException("Impossible de supprimer une commande fournisseur deja utilisee",
                    ErrorCodes.COMMANDE_FOURNISSEUR_ALREADY_IN_USE);
        }
        commandeFournisseurRepository.deleteById(id);
    }

    public CommandeFournisseur updateEtatCommande(Integer idCommande, EtatCommande etatCommande) {
        checkIdCommande(idCommande);
        if (!StringUtils.hasLength(String.valueOf(etatCommande))) {
            log.error("L'etat de la commande fournisseur is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un etat null",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
        CommandeFournisseur commandeFournisseur = checkEtatCommande(idCommande);
        commandeFournisseur.setEtatCommande(etatCommande);

        CommandeFournisseur savedCommande = commandeFournisseurRepository.save(commandeFournisseur);
        if (commandeFournisseur.isCommandeLivree()) {
            updateMvtStk(idCommande);
        }
        return commandeFournisseurRepository.save(commandeFournisseur);
    }

    public CommandeFournisseur updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);

        if (quantite == null || quantite.compareTo(BigDecimal.ZERO) == 0) {
            log.error("L'ID de la ligne commande is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une quantite null ou ZERO",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }

        CommandeFournisseur commandeFournisseur = checkEtatCommande(idCommande);
        Optional<LigneCommandeFournisseur> ligneCommandeFournisseurOptional = findLigneCommandeFournisseur(idLigneCommande);

        LigneCommandeFournisseur ligneCommandeFounisseur = ligneCommandeFournisseurOptional.get();
        ligneCommandeFounisseur.setQuantite( quantite);
        ligneCommandeFournisseurRepository.save(ligneCommandeFounisseur);

        return commandeFournisseur;
    }
    public CommandeFournisseur updateFournisseur(Integer idCommande, Integer idFournisseur) {
        checkIdCommande(idCommande);
        if (idFournisseur == null) {
            log.error("L'ID du fournisseur is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un ID fournisseur null",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
        CommandeFournisseur commandeFournisseur = checkEtatCommande(idCommande);
        Optional<Fournisseur> fournisseurOptional = fournisseurRepository.findById(idFournisseur);
        if (fournisseurOptional.isEmpty()) {
            throw new EntityNotFoundException(
                    "Aucun fournisseur n'a ete trouve avec l'ID " + idFournisseur, ErrorCodes.FOURNISSEUR_NOT_FOUND);
        }
        commandeFournisseur.setFournisseur(fournisseurOptional.get());

        return
                commandeFournisseurRepository.save(commandeFournisseur)
        ;
    }

    public CommandeFournisseur updateArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);
        checkIdArticle(idArticle, "nouvel");

        CommandeFournisseur commandeFournisseur = checkEtatCommande(idCommande);

        Optional<LigneCommandeFournisseur> ligneCommandeFournisseur = findLigneCommandeFournisseur(idLigneCommande);

        Optional<Article> articleOptional = articleRepository.findById(idArticle);
        if (articleOptional.isEmpty()) {
            throw new EntityNotFoundException(
                    "Aucune article n'a ete trouve avec l'ID " + idArticle, ErrorCodes.ARTICLE_NOT_FOUND);
        }

        List<String> errors = ArticleValidator.validate(articleOptional.get());
        if (!errors.isEmpty()) {
            throw new InvalidEntityException("Article invalid", ErrorCodes.ARTICLE_NOT_VALID, errors);
        }

        LigneCommandeFournisseur ligneCommandeFournisseurToSaved = ligneCommandeFournisseur.get();
        ligneCommandeFournisseurToSaved.setArticle(articleOptional.get());
        ligneCommandeFournisseurRepository.save(ligneCommandeFournisseurToSaved);

        return commandeFournisseur;
    }

    public CommandeFournisseur deleteArticle(Integer idCommande, Integer idLigneCommande) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);

        CommandeFournisseur commandeFournisseur = checkEtatCommande(idCommande);

        findLigneCommandeFournisseur(idLigneCommande);
        ligneCommandeFournisseurRepository.deleteById(idLigneCommande);

        return commandeFournisseur;
    }



    private void effectuerEntree(LigneCommandeFournisseur lig) {
        MvtStk mvtStk= new MvtStk();
        mvtStk.setTypeMvt(TypeMvtStk.ENTREE);
        mvtStk.setQuantite(lig.getQuantite());
        mvtStk.setDateMvt(Instant.now());
        mvtStk.setIdEntreprise(lig.getIdEntreprise());
        mvtStk.setArticle(lig.getArticle());
        mvtStk.setSourceMvt(SourceMvtStk.COMMANDE_FOURNISSEUR);

        mvtStkService.entreeStock(mvtStk);
    }

    private void checkIdCommande(Integer idCommande) {
        if (idCommande == null) {
            log.error("Commande fournisseur ID is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un ID null",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
    }

    private CommandeFournisseur checkEtatCommande(Integer idCommande) {
        CommandeFournisseur commandeFournisseur = getById(idCommande);
        if (commandeFournisseur.isCommandeLivree()) {
            throw new InvalidOperationException("Impossible de modifier la commande lorsqu'elle est livree", ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
        return commandeFournisseur;
    }

    private void updateMvtStk(Integer idCommande) {
        List<LigneCommandeFournisseur> ligneCommandeFournisseur = ligneCommandeFournisseurRepository.findByCommandeFournisseur_Id(idCommande);
        ligneCommandeFournisseur.forEach(lig -> {
            effectuerEntree(lig);
        });
    }

    private void checkIdLigneCommande(Integer idLigneCommande) {
        if (idLigneCommande == null) {
            log.error("L'ID de la ligne commande is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une ligne de commande null",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
    }
    private Optional<LigneCommandeFournisseur> findLigneCommandeFournisseur(Integer idLigneCommande) {
        Optional<LigneCommandeFournisseur> ligneCommandeFournisseurOptional = ligneCommandeFournisseurRepository.findById(idLigneCommande);
        if (ligneCommandeFournisseurOptional.isEmpty()) {
            throw new EntityNotFoundException(
                    "Aucune ligne commande fournisseur n'a ete trouve avec l'ID " + idLigneCommande, ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND);
        }
        return ligneCommandeFournisseurOptional;
    }

    private void checkIdArticle(Integer idArticle, String msg) {
        if (idArticle == null) {
            log.error("L'ID de " + msg + " is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un " + msg + " ID article null",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
    }

    /**
     * Service pour recupirer les Commande fournisseur  par page et par recherche
     * @param nom
     * @param page
     * @param size
     * @return
     */
    public Page<CommandeFournisseur> getcommandes(String nom, int page, int size){
        return commandeFournisseurRepository.findByReferenceContaining(nom,of(page,size));
    }
    /**
     * Service pour suprimer une ligne de commande
     * @param id
     */
    public void delet(Integer id){
        this.ligneCommandeFournisseurRepository.deleteById(id);
    }

    /*********************************************************************nombre de commande **********************************************************************************************/



    /**
     * Service qui retourne le nombre des CommandeFournisseur de  mois prisident
     * @return
     */

    public int countCommandeFournisseurBymouth(){
        return this.commandeFournisseurRepository.countCommandeFournisseursByMonthAndYear();
    }

    /**
     * Service qui retourne le nombre des CommandeFournisseur de  mois actuel
     * @return
     */

    public int countCommandeFournisseurByThisMouth(){
        return this.commandeFournisseurRepository.countCommandeFournisseursByThisMonthAndYear();
    }


    /**
     * Service qui retourne le nombre des CommandeFournisseur de cette année
     * @return
     */
    public int countCommandeFournisseurByYear(){
        return this.commandeFournisseurRepository.countCommandeFournisseursByYear();
    }

    /**
     * Service qui retourne le nombre des CommandeFournisseur de l' année président
     * @return
     */
    public int countCommandeFournisseurByLastYear(){
        return this.commandeFournisseurRepository.countCommandeFournisseursByLastYear();
    }



    /**
     * Service qui retourne le nombre des CommandeFournisseur de aujourd'huit
     * @return
     */
    public int countCommandeFournisseurByDay(){
        return this.commandeFournisseurRepository.countCommandeFournisseursByDay();
    }
    /**
     * Service qui retourne le nombre des CommandeFournisseur d'hier
     * @return
     */
    public int countCommandeFournisseurByLastDay(){
        LocalDate yesterdayDate = LocalDate.now().minusDays(1);
        Timestamp yesterdayTimestamp = Timestamp.valueOf(yesterdayDate.atStartOfDay());
        return this.commandeFournisseurRepository.countCommandeFournisseursByYesterday(yesterdayTimestamp);
    }

 /****************************************************************************************************** REVENUE***********************************************************/



    /**
     * Service qui retourne le revenue des CommandeFournisseur de  mois actuel
     * @return
     */

    public int sumCommandeFournisseurBymouth(){
        return this.commandeFournisseurRepository.sumCommandeFournisseursByMonthAndYear();
    }

    /**
     * Service qui retourne le revenue des CommandeFournisseur de  mois president
     * @return
     */

    public int sumCommandeFournisseurByLastMouth(){
        return this.commandeFournisseurRepository.sumCommandeFournisseursByLastMonthAndYear();
    }


    /**
     * Service qui retourne le revenue des CommandeFournisseur de cette année
     * @return
     */
    public int sumCommandeFournisseurByYear(){
        return this.commandeFournisseurRepository.sumCommandeFournisseursByYear();
    }

    /**
     * Service qui retourne le revenue des CommandeFournisseur de l' année président
     * @return
     */
    public int sumCommandeFournisseurByLastYear(){
        return this.commandeFournisseurRepository.sumCommandeFournisseursByLastYear();
    }



    /**
     * Service qui retourne le revenue des CommandeFournisseur de aujourd'huit
     * @return
     */
    public int sumCommandeFournisseurByDay(){
        return this.commandeFournisseurRepository.sumCommandeFournisseursByDay();
    }
    /**
     * Service qui retourne le revenue des CommandeFournisseur d'hier
     * @return
     */
    public int sumCommandeFournisseurByLastDay(){
        LocalDate yesterdayDate = LocalDate.now().minusDays(1);
        Timestamp yesterdayTimestamp = Timestamp.valueOf(yesterdayDate.atStartOfDay());
        return this.commandeFournisseurRepository.sumCommandeFournisseursByYesterday(yesterdayTimestamp);
    }

    /***********************************COMMANDE FOURNISSEURPAR ORDER DEC ************************************************************************/


    /**
     * Service qui retourne les CommandeFournisseur par order dec de  mois actuel
     *
     * @return
     */

    @Transactional
    public List<CommandeClientStats>  CmdFrsByMonthByOrderByTotalPrixDesc() {

        String sql = "SELECT c.id AS idCommande, clt.prenom , clt.nom, c.etatCommande AS status, c.total_prix AS total, "
                + "GROUP_CONCAT(ar.designation ) "
                + "FROM commande_fournisseur c "
                + "JOIN ligne_commande_fournisseur l "
                + "ON c.id = l.idcommande_fournisseur "
                + "JOIN article ar "
                + "ON l.idarticle = ar.id "
                + "JOIN fournisseur clt "
                + "ON c.idfournisseur = clt.id "
                + "WHERE YEAR(c.create_date) = YEAR(CURRENT_DATE) "
                + "AND MONTH(c.create_date) = MONTH(CURRENT_DATE) "
                + "GROUP BY c.id "
                + "ORDER BY total DESC LIMIT 5 ";

        List<CommandeClientStats> resultList = new ArrayList<>();
        entityManager.createNativeQuery(sql, CommandeClientStats.class)
                .getResultList()
                .forEach(elm -> resultList.add((CommandeClientStats) elm));
        return resultList;

    }


    /**
     * Service qui retourne les CommandeFournisseur  par order dec de  mois president
     *
     * @return
     */

    public List<CommandeClientStats> CmdFrsByLastMonthByOrderByTotalPrixDesc() {
        String sql = "SELECT c.id AS idCommande, clt.prenom , clt.nom, c.etatCommande AS status, c.total_prix AS total, "
                + "GROUP_CONCAT(ar.designation ) "
                + "FROM commande_fournisseur c "
                + "JOIN ligne_commande_fournisseur l "
                + "ON c.id = l.idcommande_fournisseur "
                + "JOIN article ar "
                + "ON l.idarticle = ar.id "
                + "JOIN fournisseur clt "
                + "ON c.idfournisseur = clt.id "
                + "WHERE YEAR(c.create_date) = YEAR(CURRENT_DATE) "
                + "AND MONTH(c.create_date) = (MONTH(CURRENT_DATE)-1) "
                + "GROUP BY c.id "
                + "ORDER BY total DESC LIMIT 5 ";

        List<CommandeClientStats> resultList = new ArrayList<>();
        entityManager.createNativeQuery(sql, CommandeClientStats.class)
                .getResultList()
                .forEach(elm -> resultList.add((CommandeClientStats) elm));
        return resultList;
    }


    /**
     * Service qui retourne les CommandeFournisseur  par order decde cette année
     *
     * @return
     */
    public List<CommandeClientStats> CmdFrsByYearByOrderByTotalPrixDesc() {
        String sql = "SELECT c.id AS idCommande, clt.prenom , clt.nom, c.etatCommande AS status, c.total_prix AS total, "
                + "GROUP_CONCAT(ar.designation ) "
                + "FROM commande_fournisseur c "
                + "JOIN ligne_commande_fournisseur l "
                + "ON c.id = l.idcommande_fournisseur "
                + "JOIN article ar "
                + "ON l.idarticle = ar.id "
                + "JOIN fournisseur clt "
                + "ON c.idfournisseur = clt.id "
                + "WHERE YEAR(c.create_date) = YEAR(CURRENT_DATE) "
                + "GROUP BY c.id "
                + "ORDER BY total DESC LIMIT 5 ";

        List<CommandeClientStats> resultList = new ArrayList<>();
        entityManager.createNativeQuery(sql, CommandeClientStats.class)
                .getResultList()
                .forEach(elm -> resultList.add((CommandeClientStats) elm));
        return resultList;

    }

    /**
     * Service qui retourne les CommandeFournisseur  par order dec de l' année président
     *
     * @return
     */
    public List<CommandeClientStats> CmdFrsByLastYearByOrderByTotalPrixDesc() {
        String sql = "SELECT c.id AS idCommande, clt.prenom , clt.nom, c.etatCommande AS status, c.total_prix AS total, "
                + "GROUP_CONCAT(ar.designation ) "
                + "FROM commande_fournisseur c "
                + "JOIN ligne_commande_fournisseur l "
                + "ON c.id = l.idcommande_fournisseur "
                + "JOIN article ar "
                + "ON l.idarticle = ar.id "
                + "JOIN fournisseur clt "
                + "ON c.idfournisseur = clt.id "
                + "WHERE YEAR(c.create_date) = (YEAR(CURRENT_DATE)-1) "
                + "GROUP BY c.id "
                + "ORDER BY total DESC LIMIT 5 ";
        List<CommandeClientStats> resultList = new ArrayList<>();
        entityManager.createNativeQuery(sql, CommandeClientStats.class)
                .getResultList()
                .forEach(elm -> resultList.add((CommandeClientStats) elm));
        return resultList;
    }


    /**
     * Service qui retourne les CommandeFournisseur  par order dec de aujourd'huit
     *
     * @return
     */
    public List<CommandeClientStats> CmdFrsByDayByOrderByTotalPrixDesc() {
        String sql = "SELECT c.id AS idCommande, clt.prenom , clt.nom, c.etatCommande AS status, c.total_prix AS total, "
                + "GROUP_CONCAT(ar.designation ) "
                + "FROM commande_fournisseur c "
                + "JOIN ligne_commande_fournisseur l "
                + "ON c.id = l.idcommande_fournisseur "
                + "JOIN article ar "
                + "ON l.idarticle = ar.id "
                + "JOIN fournisseur clt "
                + "ON c.idfournisseur = clt.id "
                + "WHERE YEAR(c.create_date) = YEAR(CURRENT_DATE) AND  DATE(c.create_date) = CURRENT_DATE  "
                + "GROUP BY c.id "
                + "ORDER BY total DESC LIMIT 5 ";
        List<CommandeClientStats> resultList = new ArrayList<>();
        entityManager.createNativeQuery(sql, CommandeClientStats.class)
                .getResultList()
                .forEach(elm -> resultList.add((CommandeClientStats) elm));
        return resultList;

    }

    /**
     * Service qui retourne les CommandeFournisseur  par order dec d'hier
     *
     * @return
     */
    public List<CommandeClientStats> CmdFrsByLastDayByOrderByTotalPrixDesc() {
        LocalDate yesterdayDate = LocalDate.now().minusDays(1);
        Timestamp yesterdayTimestamp = Timestamp.valueOf(yesterdayDate.atStartOfDay());
        String sql = "SELECT c.id AS idCommande, clt.prenom , clt.nom, c.etatCommande AS status, c.total_prix AS total, "
                + "GROUP_CONCAT(ar.designation ) "
                + "FROM commande_fournisseur c "
                + "JOIN ligne_commande_fournisseur l "
                + "ON c.id = l.idcommande_fournisseur "
                + "JOIN article ar "
                + "ON l.idarticle = ar.id "
                + "JOIN fournisseur clt "
                + "ON c.idfournisseur = clt.id "
                + "WHERE YEAR(c.create_date) = YEAR(CURRENT_DATE) AND  DATE(c.create_date) = '" + yesterdayTimestamp + "' "
                + "GROUP BY c.id "
                + "ORDER BY total DESC LIMIT 5 ";

        List<CommandeClientStats> resultList = new ArrayList<>();
        entityManager.createNativeQuery(sql, CommandeClientStats.class)
                .getResultList()
                .forEach(elm -> resultList.add((CommandeClientStats) elm));
        return resultList;

    }

}