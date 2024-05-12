package com.tahiri.gestiondestock.service;


import com.tahiri.gestiondestock.exception.EntityNotFoundException;
import com.tahiri.gestiondestock.exception.ErrorCodes;
import com.tahiri.gestiondestock.exception.InvalidEntityException;
import com.tahiri.gestiondestock.exception.InvalidOperationException;
import com.tahiri.gestiondestock.model.*;
import com.tahiri.gestiondestock.repository.*;
import com.tahiri.gestiondestock.validator.ArticleValidator;
import com.tahiri.gestiondestock.validator.CommandeClientValidator;
import com.tahiri.gestiondestock.validator.LigneCommadeClientValidator;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.EntityResult;
import jakarta.persistence.FieldResult;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


import static org.springframework.data.domain.PageRequest.of;

@Service
@Slf4j
public class CommandeClientService {
    String identreprise = MDC.get("idEntreprise");
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private CommandeClientRepository commandeClientRepository;
    @Autowired
    private LigneCommandeClientRepository ligneCommandeClientRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired

    private ArticleRepository articleRepository;


    @Autowired
    private MvtStkService mvtStkService;

    public CommandeClient save(CommandeClient commandeClient) {

        List<String> errors = CommandeClientValidator.validate(commandeClient);


        if (commandeClient.getId() != null &&  "LIVREE".equals(this.etatCommande(commandeClient.getId()) )) {

            throw new InvalidOperationException("Impossible de modifier la commande lorsqu'elle est livree", ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        Optional<Client> client = clientRepository.findById(commandeClient.getClient().getId());
        if (client.isEmpty()) {
            log.warn("Client with ID {} was not found in the DB", commandeClient.getClient().getId());
            throw new EntityNotFoundException("Aucun client avec l'ID" + commandeClient.getClient().getId() + " n'a ete trouve dans la BDD",
                    ErrorCodes.CLIENT_NOT_FOUND);
        }
        List<String> articleErrors = new ArrayList<>();


        if (commandeClient.getLigneCommandeClients() != null) {
            commandeClient.getLigneCommandeClients().forEach(ligCmdClt -> {

                        if (ligCmdClt.getArticle() != null) {
                            Optional<Article> article = articleRepository.findById(ligCmdClt.getArticle().getId());
                            if (article.isEmpty()) {
                                commandeClient.getLigneCommandeClients().forEach(lig -> {
                                    LigneCommadeClientValidator.validate(lig).forEach(elm -> {
                                        errors.add(elm);
                                    });
                                });
                                articleErrors.add("L'article avec l'ID " + ligCmdClt.getArticle().getId() + " n'existe pas");
                            }

                        } else {
                            commandeClient.getLigneCommandeClients().forEach(lig -> {
                                LigneCommadeClientValidator.validate(lig).forEach(elm -> {
                                    errors.add(elm);
                                });
                            });
                            articleErrors.add("Impossible d'enregister une commande avec un aticle NULL");
                        }
                    }
            );
        }


        if (!articleErrors.isEmpty()) {
            log.warn("");
            throw new InvalidEntityException("Article n'existe pas dans la BDD", ErrorCodes.ARTICLE_NOT_FOUND, articleErrors);
        }

        commandeClient.setDateCommande(Instant.now());
        CommandeClient saveCmdClt = commandeClientRepository.save(commandeClient);
        if (commandeClient.getLigneCommandeClients() != null) {
            commandeClient.getLigneCommandeClients().forEach(ligCmdClt -> {
                if (ligCmdClt.getId() !=null){
                Optional<LigneCommandeClient> optLigneCmdClt = this.ligneCommandeClientRepository.findById(ligCmdClt.getId());
                            if (optLigneCmdClt.isPresent()) {
                                MvtStk mvtStk = this.mvtStkService.findByIdLigneCltFrs(ligCmdClt.getId());
                                if (mvtStk != null) {
                                    ligCmdClt.setId(ligCmdClt.getId());
                                    ligCmdClt.setCommandeClient(ligCmdClt.getCommandeClient());
                                    ligCmdClt.setArticle(ligCmdClt.getArticle());
                                    ligCmdClt.setPrixUnitaire(ligCmdClt.getPrixUnitaire());
                                    ligCmdClt.setQuantite(ligCmdClt.getQuantite());
                                    ligCmdClt.setIdEntreprise(ligCmdClt.getIdEntreprise());
                                    ligCmdClt.setLastModifiedDate(new Date());
                                  this.ligneCommandeClientRepository.save(ligCmdClt);
                                  mvtStk.setId(mvtStk.getId());
                                  mvtStk.setTypeMvt(mvtStk.getTypeMvt());
                                  mvtStk.setSourceMvt(mvtStk.getSourceMvt());
                                  mvtStk.setDateMvt(mvtStk.getDateMvt());
                                  mvtStk.setArticle(ligCmdClt.getArticle());
                                  mvtStk.setQuantite(ligCmdClt.getQuantite());
                                  mvtStk.setIdEntreprise(mvtStk.getIdEntreprise());
                                  mvtStk.setIdLignefrsclt(mvtStk.getIdLignefrsclt());
                                  mvtStk.setLastModifiedDate(new Date());
                              mvtStkService.sortieStock(mvtStk);
                                }
                            } }else {

                                LigneCommandeClient ligneCommandeClient = ligneCommandeClientRepository.save(ligCmdClt);
                                ligneCommandeClient.setCommandeClient(saveCmdClt);
                                ligneCommandeClient.setIdEntreprise(saveCmdClt.getIdEntreprise());
                                LigneCommandeClient savedLigneCmd = ligneCommandeClientRepository.save(ligneCommandeClient);
                                effectuerSortie(savedLigneCmd);
                            }
                    }

            );

        }


        return commandeClientRepository.save(saveCmdClt);
    }


    public String etatCommande( Integer id){
        return this.commandeClientRepository.findEtatCommandeById(id);
    }

    public List<CommandeClient> getAll() {

        return commandeClientRepository.findAll();
    }

    public CommandeClient getById(Integer id) {
        if (id == null) {
            log.error("Commande client ID is NULL");
            return null;
        }
        return commandeClientRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucune commande client n'a ete trouve avec l'ID " + id, ErrorCodes.COMMANDE_CLIENT_NOT_FOUND
                ));
    }

    public CommandeClient findByCode(String reference) {
        if (!StringUtils.hasLength(reference)) {
            log.error("Commande client CODE is NULL");
            return null;
        }
        return commandeClientRepository.findByReference(reference).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucune commande client n'a ete trouve avec le CODE " + reference, ErrorCodes.COMMANDE_CLIENT_NOT_FOUND
                ));


    }


    public void delete(Integer id) {
        if (id == null) {
            log.error("Commande client ID is NULL");
            return;
        }
        List<LigneCommandeClient> ligneCommadeClients = ligneCommandeClientRepository.findByCommandeClient_Id(id);
        if (!ligneCommadeClients.isEmpty()) {
            throw new InvalidOperationException("Impossible de supprimer une commande client deja utilisee",
                    ErrorCodes.COMMANDE_CLIENT_ALREADY_IN_USE);
        }
        commandeClientRepository.deleteById(id);
    }

    public List<LigneCommandeClient> findAllLignesCommandesByCommandeClient(Integer idCommande) {

        return ligneCommandeClientRepository.findByCommandeClient_Id(idCommande);
    }

    public CommandeClient updateEtatCommde(Integer idCommande, EtatCommande etatCommande) {
        checkIdCommande(idCommande);
        if (!StringUtils.hasLength(String.valueOf(etatCommande))) {
            log.error("L'etat de la commande client is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un etat null",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        CommandeClient commandeClient = checkEtatCommande(idCommande);
        commandeClient.setEtatCommande(etatCommande);

        CommandeClient savedCmdClt = commandeClientRepository.save(commandeClient);
        if (commandeClient.isCommandeLivree()) {
            updateMvtStk(idCommande);
        }
        return commandeClientRepository.save(savedCmdClt);
    }

    public CommandeClient updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);

        if (quantite == null || quantite.compareTo(BigDecimal.ZERO) == 0) {
            log.error("L'ID de la ligne commande is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une quantite null ou ZERO",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
        CommandeClient commandeClient = checkEtatCommande(idCommande);

        Optional<LigneCommandeClient> ligneCommadeClientOptional = findLigneCommandeClient(idLigneCommande);
        LigneCommandeClient ligneCommandeClient = ligneCommadeClientOptional.get();
        ligneCommandeClient.setQuantite(quantite);
        ligneCommandeClientRepository.save(ligneCommandeClient);

        return commandeClient;
    }

    public CommandeClient updateClient(Integer idCommande, Integer idClient) {
        checkIdCommande(idCommande);
        if (idClient == null) {
            log.error("L'ID du client is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un ID client null",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
        CommandeClient commandeClient = checkEtatCommande(idCommande);
        Optional<Client> clientOptional = clientRepository.findById(idClient);
        if (clientOptional.isEmpty()) {
            throw new EntityNotFoundException(
                    "Aucun client n'a ete trouve avec l'ID " + idClient, ErrorCodes.CLIENT_NOT_FOUND);
        }
        commandeClient.setClient(clientOptional.get());

        return commandeClientRepository.save(commandeClient);
    }


    public CommandeClient updatArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);
        checkIdArticle(idArticle, "nouvel");
        CommandeClient commandeClient = checkEtatCommande(idCommande);
        Optional<LigneCommandeClient> ligneCommandeClient = findLigneCommandeClient(idLigneCommande);
        Optional<Article> articleOptional = articleRepository.findById(idArticle);
        if (articleOptional.isEmpty()) {
            throw new EntityNotFoundException(
                    "Aucune article n'a ete trouve avec l'ID " + idArticle, ErrorCodes.ARTICLE_NOT_FOUND);
        }
        List<String> errors = ArticleValidator.validate(articleOptional.get());
        if (!errors.isEmpty()) {
            throw new InvalidEntityException("Article invalid", ErrorCodes.ARTICLE_NOT_VALID, errors);
        }
        LigneCommandeClient ligneCommandeClientToSaved = ligneCommandeClient.get();
        ligneCommandeClientToSaved.setArticle(articleOptional.get());
        ligneCommandeClientRepository.save(ligneCommandeClientToSaved);
        return commandeClient;
    }

    public CommandeClient deleteArticle(Integer idCommande, Integer idLigneCommande) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);

        CommandeClient commandeClient = checkEtatCommande(idCommande);
        // Just to check the LigneCommandeClient and inform the client in case it is absent
        findLigneCommandeClient(idLigneCommande);
        ligneCommandeClientRepository.deleteById(idLigneCommande);

        return commandeClient;
    }
 public  boolean LigneExiste(Integer id){
    Optional<LigneCommandeClient>  ligneCommandeClient  =ligneCommandeClientRepository.findById(id);
    return ligneCommandeClient.isPresent();
 }
    private void effectuerSortie(LigneCommandeClient lig) {
        MvtStk mvtStk = new MvtStk();
        mvtStk.setTypeMvt(TypeMvtStk.SORTIE);
        mvtStk.setQuantite(lig.getQuantite());
        mvtStk.setDateMvt(Instant.now());
       mvtStk.setIdLignefrsclt(lig.getId());
        mvtStk.setIdEntreprise(lig.getIdEntreprise());
        mvtStk.setArticle(lig.getArticle());
        mvtStk.setSourceMvt(SourceMvtStk.COMMANDE_CLIENT);

        mvtStkService.sortieStock(mvtStk);
    }

    private void checkIdCommande(Integer idCommande) {
        if (idCommande == null) {
            log.error("Commande client ID is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un ID null",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
    }

    private CommandeClient checkEtatCommande(Integer idCommande) {
        CommandeClient commandeClient = getById(idCommande);
        if (commandeClient.isCommandeLivree()) {
            throw new InvalidOperationException("Impossible de modifier la commande lorsqu'elle est livree", ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
        return commandeClient;
    }

    private void updateMvtStk(Integer idCommande) {
        List<LigneCommandeClient> ligneCommandeClients = ligneCommandeClientRepository.findByCommandeClient_Id(idCommande);
        ligneCommandeClients.forEach(lig -> {
            effectuerSortie(lig);
        });
    }

    private void checkIdLigneCommande(Integer idLigneCommande) {
        if (idLigneCommande == null) {
            log.error("L'ID de la ligne commande is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une ligne de commande null",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
    }

    private Optional<LigneCommandeClient> findLigneCommandeClient(Integer idLigneCommande) {
        Optional<LigneCommandeClient> ligneCommandeClientOptional = ligneCommandeClientRepository.findById(idLigneCommande);
        if (ligneCommandeClientOptional.isEmpty()) {
            throw new EntityNotFoundException(
                    "Aucune ligne commande client n'a ete trouve avec l'ID " + idLigneCommande, ErrorCodes.COMMANDE_CLIENT_NOT_FOUND);
        }
        return ligneCommandeClientOptional;
    }

    /**
     * Service pour suprimer une ligne de commande
     * @param id
     */
    public void delet(Integer id){
         this.ligneCommandeClientRepository.deleteById(id);
    }

    private void checkIdArticle(Integer idArticle, String msg) {
        if (idArticle == null) {
            log.error("L'ID de " + msg + " is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un " + msg + " ID article null",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
    }

    /**
     * Service pour recupirer les Commande Client  par page et par recherche
     *
     * @param nom
     * @param page
     * @param size
     * @return
     */
    public Page<CommandeClient> getcommandes(String nom, int page, int size) {
        String identreprise = MDC.get("idEntreprise");
        return commandeClientRepository.findByReferenceContainingAndIdEntreprise(nom,Integer.valueOf(identreprise), of(page, size));
    }


    /*********************************************************************************  nombre de commande *********************************************************************/


    /**
     * Service qui retourne le nombre des CommandeClient de  mois prisident
     *
     * @return
     */

    public int countCommandeClientBymouth() {
        String identreprise = MDC.get("idEntreprise");
        return this.commandeClientRepository.countCommandeClientsByMonthAndYear(identreprise);
    }

    /**
     * Service qui retourne le nombre des CommandeClient de  mois actuel
     *
     * @return
     */

    public int countCommandeClientByThisMouth() {
        String identreprise = MDC.get("idEntreprise");
        return this.commandeClientRepository.countCommandeClientsByThisMonthAndYear(identreprise);
    }


    /**
     * Service qui retourne le nombre des CommandeClient de cette année
     *
     * @return
     */
    public int countCommandeClientByYear() {
        String identreprise = MDC.get("idEntreprise");
        return this.commandeClientRepository.countCommandeClientsByYear(identreprise);
    }

    /**
     * Service qui retourne le nombre des CommandeClient de l' année président
     *
     * @return
     */
    public int countCommandeClientByLastYear() {
        String identreprise = MDC.get("idEntreprise");
        return this.commandeClientRepository.countCommandeClientsByLastYear(identreprise);
    }


    /**
     * Service qui retourne le nombre des CommandeClient de aujourd'huit
     *
     * @return
     */
    public int countCommandeClientByDay() {
        String identreprise = MDC.get("idEntreprise");
        return this.commandeClientRepository.countCommandeClientsByDay(identreprise);
    }

    /**
     * Service qui retourne le nombre des CommandeClient d'hier
     *
     * @return
     */
    public int countCommandeClientByLastDay() {
        String identreprise = MDC.get("idEntreprise");
        LocalDate yesterdayDate = LocalDate.now().minusDays(1);
        Timestamp yesterdayTimestamp = Timestamp.valueOf(yesterdayDate.atStartOfDay());
        return this.commandeClientRepository.countCommandeClientsByYesterday(yesterdayTimestamp,identreprise);
    }


    /************************************************************************  REVENUE    **********************************************************/


    /**
     * Service qui retourne le revenue des CommandeClient de  mois actuel
     *
     * @return
     */

    public int sumCommandeClientBymouth() {
        String identreprise = MDC.get("idEntreprise");
        return this.commandeClientRepository.sumCommandeClientsByMonthAndYear(identreprise);
    }

    /**
     * Service qui retourne le revenue des CommandeClient de  mois president
     *
     * @return
     */

    public int sumCommandeClientByLastMouth() {
        String identreprise = MDC.get("idEntreprise");
        return this.commandeClientRepository.sumCommandeClientsByLastMonthAndYear(identreprise);
    }


    /**
     * Service qui retourne le revenue des CommandeClient de cette année
     *
     * @return
     */
    public int sumCommandeClientByYear() {
        String identreprise = MDC.get("idEntreprise");
        return this.commandeClientRepository.sumCommandeClientsByYear(identreprise);
    }

    /**
     * Service qui retourne le revenue des CommandeClient de l' année président
     *
     * @return
     */
    public int sumCommandeClientByLastYear() {
        String identreprise = MDC.get("idEntreprise");
        return this.commandeClientRepository.sumCommandeClientsByLastYear(identreprise);
    }


    /**
     * Service qui retourne le revenue des CommandeClient de aujourd'huit
     *
     * @return
     */
    public int sumCommandeClientByDay() {
        String identreprise = MDC.get("idEntreprise");
        return this.commandeClientRepository.sumCommandeClientsByDay(identreprise);
    }

    /**
     * Service qui retourne le revenue des CommandeClient d'hier
     *
     * @return
     */
    public int sumCommandeClientByLastDay() {
        String identreprise = MDC.get("idEntreprise");
        LocalDate yesterdayDate = LocalDate.now().minusDays(1);
        Timestamp yesterdayTimestamp = Timestamp.valueOf(yesterdayDate.atStartOfDay());

        return this.commandeClientRepository.sumCommandeClientsByYesterday(yesterdayTimestamp,identreprise);
    }


    /**************************************************COMMANDE CLIENT PAR ORDER DESC TOTALPRIX *******************************************************/

    /**
     * Service qui retourne les CommandeClient  par order dec de  mois actuel
     *
     * @return
     */

    @Transactional
    public List<CommandeClientStats> CmdCltByMonthByOrderByTotalPrixDesc( String identreprise) {

        String sql = "SELECT c.id AS idCommande, clt.prenom , clt.nom, c.etatCommande AS status, c.total_prix AS total, "
                + "GROUP_CONCAT(ar.designation ) "
                + "FROM commande_client c "
                + "JOIN ligne_commande_client l "
                + "ON c.id = l.idcommande_client "
                + "JOIN article ar "
                + "ON l.idarticle = ar.id "
                + "JOIN client clt "
                + "ON c.idclient = clt.id "
                + "WHERE YEAR(c.create_date) = YEAR(CURRENT_DATE) "
                + "AND MONTH(c.create_date) = MONTH(CURRENT_DATE) AND l.identreprise = :identreprise   "
                + "GROUP BY c.id "
                + "ORDER BY total DESC LIMIT 5 ";

        List<CommandeClientStats> resultList = new ArrayList<>();
        entityManager.createNativeQuery(sql, CommandeClientStats.class)
                .setParameter("identreprise", identreprise)
                .getResultList()
                .forEach(elm -> resultList.add((CommandeClientStats) elm));
        return resultList;

    }


    /**
     * Service qui retourne les CommandeClient  par order dec de  mois president
     *
     * @return
     */

    public List<CommandeClientStats> CmdCltByLastMonthByOrderByTotalPrixDesc( String identreprise) {

        String sql = "SELECT c.id AS idCommande, clt.prenom , clt.nom, c.etatCommande AS status, c.total_prix AS total, "
                + "GROUP_CONCAT(ar.designation ) "
                + "FROM commande_client c "
                + "JOIN ligne_commande_client l "
                + "ON c.id = l.idcommande_client "
                + "JOIN article ar "
                + "ON l.idarticle = ar.id "
                + "JOIN client clt "
                + "ON c.idclient = clt.id "
                + "WHERE YEAR(c.create_date) = YEAR(CURRENT_DATE) "
                + "AND MONTH(c.create_date) = (MONTH(CURRENT_DATE)-1) AND l.identreprise = :identreprise   "
                + "GROUP BY c.id "
                + "ORDER BY total DESC LIMIT 5 ";

        List<CommandeClientStats> resultList = new ArrayList<>();
        entityManager.createNativeQuery(sql, CommandeClientStats.class)
                .setParameter("identreprise", identreprise)
                .getResultList()
                .forEach(elm -> resultList.add((CommandeClientStats) elm));
        return resultList;
    }


    /**
     * Service qui retourne les CommandeClient  par order decde cette année
     *
     * @return
     */
    public List<CommandeClientStats> CmdCltByYearByOrderByTotalPrixDesc( String identreprise) {

        String sql = "SELECT c.id AS idCommande, clt.prenom , clt.nom, c.etatCommande AS status, c.total_prix AS total, "
                + "GROUP_CONCAT(ar.designation ) "
                + "FROM commande_client c "
                + "JOIN ligne_commande_client l "
                + "ON c.id = l.idcommande_client "
                + "JOIN article ar "
                + "ON l.idarticle = ar.id "
                + "JOIN client clt "
                + "ON c.idclient = clt.id "
                + "WHERE YEAR(c.create_date) = YEAR(CURRENT_DATE) AND l.identreprise = :identreprise   "
                + "GROUP BY c.id "
                + "ORDER BY total DESC LIMIT 5 ";

        List<CommandeClientStats> resultList = new ArrayList<>();
        entityManager.createNativeQuery(sql, CommandeClientStats.class)
                .setParameter("identreprise",identreprise)
                .getResultList()
                .forEach(elm -> resultList.add((CommandeClientStats) elm));
        return resultList;

    }

    /**
     * Service qui retourne les CommandeClient  par order dec de l' année président
     *
     * @return
     */
    public List<CommandeClientStats> CmdCltByLastYearByOrderByTotalPrixDesc( String identreprise) {

        String sql = "SELECT c.id AS idCommande, clt.prenom , clt.nom, c.etatCommande AS status, c.total_prix AS total, "
                + "GROUP_CONCAT(ar.designation ) "
                + "FROM commande_client c "
                + "JOIN ligne_commande_client l "
                + "ON c.id = l.idcommande_client "
                + "JOIN article ar "
                + "ON l.idarticle = ar.id "
                + "JOIN client clt "
                + "ON c.idclient = clt.id "
                + "WHERE YEAR(c.create_date) = (YEAR(CURRENT_DATE)-1) AND l.identreprise = :identreprise  "
                + "GROUP BY c.id "
                + "ORDER BY total DESC LIMIT 5 ";
        List<CommandeClientStats> resultList = new ArrayList<>();
        entityManager.createNativeQuery(sql, CommandeClientStats.class)
                .setParameter("identreprise",identreprise)
                .getResultList()
                .forEach(elm -> resultList.add((CommandeClientStats) elm));
        return resultList;
    }


    /**
     * Service qui retourne les CommandeClient  par order dec de aujourd'huit
     *
     * @return
     */
    public List<CommandeClientStats> CmdCltByDayByOrderByTotalPrixDesc(String identreprise) {
        String sql = "SELECT c.id AS idCommande, clt.prenom , clt.nom, c.etatCommande AS status, c.total_prix AS total, "
                + "GROUP_CONCAT(ar.designation ) "
                + "FROM commande_client c "
                + "JOIN ligne_commande_client l "
                + "ON c.id = l.idcommande_client "
                + "JOIN article ar "
                + "ON l.idarticle = ar.id "
                + "JOIN client clt "
                + "ON c.idclient = clt.id "
                + "WHERE YEAR(c.create_date) = YEAR(CURRENT_DATE) AND DATE(c.create_date) = CURRENT_DATE  AND l.identreprise = :identreprise   "
                + "GROUP BY c.id "
                + "ORDER BY total DESC LIMIT 5 ";
        List<CommandeClientStats> resultList = new ArrayList<>();
        entityManager.createNativeQuery(sql, CommandeClientStats.class)
                .setParameter("identreprise", identreprise)
                .getResultList()
                .forEach(elm -> resultList.add((CommandeClientStats) elm));
        return resultList;

    }

    /**
     * Service qui retourne les CommandeClient  par order dec d'hier
     *
     * @return
     */
    public List<CommandeClientStats> CmdCltByLastDayByOrderByTotalPrixDesc( String identreprise) {

        LocalDate yesterdayDate = LocalDate.now().minusDays(1);
        Timestamp yesterdayTimestamp = Timestamp.valueOf(yesterdayDate.atStartOfDay());
        String sql = "SELECT c.id AS idCommande, clt.prenom , clt.nom, c.etatCommande AS status, c.total_prix AS total, "
                + "GROUP_CONCAT(ar.designation ) "
                + "FROM commande_client c "
                + "JOIN ligne_commande_client l "
                + "ON c.id = l.idcommande_client "
                + "JOIN article ar "
                + "ON l.idarticle = ar.id "
                + "JOIN client clt "
                + "ON c.idclient = clt.id "
                + "WHERE YEAR(c.create_date) = YEAR(CURRENT_DATE) AND  DATE(c.create_date) = '" + yesterdayTimestamp + "'  AND l.identreprise = :identreprise   "
                + "GROUP BY c.id "
                + "ORDER BY total DESC LIMIT 5 ";

        List<CommandeClientStats> resultList = new ArrayList<>();
        entityManager.createNativeQuery(sql, CommandeClientStats.class)
                .setParameter("identreprise", identreprise)
                .getResultList()
                .forEach(elm -> resultList.add((CommandeClientStats) elm));
        return resultList;

    }


    /*************************************************************** Top arcticle demander *********************************************************************/

    /**
     * Service qui retourne top articles de aujourd'huit
     *
     * @return
     */
    public List<ArticleStats> getTopArticlesByCommandesToDay() {
        String identreprise = MDC.get("idEntreprise");
        return this.ligneCommandeClientRepository.findTopArticlesByCommandesToDay(identreprise);
    }


    /**
     * Service qui retourne top articles de  mois actuel
     *
     * @return
     */


    public List<ArticleStats> getTopArticlesByCommandesToMonth() {
        String identreprise = MDC.get("idEntreprise");
        return this.ligneCommandeClientRepository.findTopArticlesByCommandesToMonth(identreprise);
    }

    /**
     * Service qui retourne top articles cette année
     *
     * @return
     */
    public List<ArticleStats> getTopArticlesByCommandesToYear() {
        String identreprise = MDC.get("idEntreprise");
        return this.ligneCommandeClientRepository.findTopArticlesByCommandesToYear(identreprise);
    }

}
