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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CommandeClientService {


    @Autowired
    private CommandeClientRepository commandeClientRepository;
    @Autowired
    private LigneCommandeClientRepository ligneCommandeClientRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired

    private ArticleRepository articleRepository;


    @Autowired
    private  MvtStkService mvtStkService;

    public CommandeClient save(CommandeClient commandeClient) {

        List<String> errors = CommandeClientValidator.validate(commandeClient);




        if (commandeClient.getId() != null && commandeClient.isCommandeLivree()) {

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
                        commandeClient.getLigneCommandeClients().forEach(lig->{
                            LigneCommadeClientValidator.validate(lig).forEach(elm->{
                                errors.add(elm);
                            });
                        });
                        articleErrors.add("L'article avec l'ID " + ligCmdClt.getArticle().getId() + " n'existe pas");
                    }

                }else {
                    commandeClient.getLigneCommandeClients().forEach(lig->{
                        LigneCommadeClientValidator.validate(lig).forEach(elm->{
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

            commandeClient.getLigneCommandeClients().forEach(ligCmdClt->{
                LigneCommandeClient ligneCommandeClient = ligneCommandeClientRepository.save(ligCmdClt);
                ligneCommandeClient.setCommandeClient(saveCmdClt);
                ligneCommandeClient.setIdEntreprise(saveCmdClt.getIdEntreprise());
                LigneCommandeClient savedLigneCmd = ligneCommandeClientRepository.save(ligneCommandeClient);
                effectuerSortie(savedLigneCmd);

                    }
            );

        }


        return commandeClientRepository.save(saveCmdClt);
}


public List<CommandeClient> getAll() {

    return commandeClientRepository.findAll();
}

public CommandeClient getById(Integer id) {
    if (id == null) {
        log.error("Commande client ID is NULL");
        return null;
    }
    return commandeClientRepository.findById(id).orElseThrow(()->
            new EntityNotFoundException(
                    "Aucune commande client n'a ete trouve avec l'ID " + id, ErrorCodes.COMMANDE_CLIENT_NOT_FOUND
            ));
}

public  CommandeClient findByCode(String reference){
    if (!StringUtils.hasLength(reference)) {
        log.error("Commande client CODE is NULL");
        return null;
    }
    return commandeClientRepository.findByReference(reference).orElseThrow(()->
            new EntityNotFoundException(
                    "Aucune commande client n'a ete trouve avec le CODE " + reference, ErrorCodes.COMMANDE_CLIENT_NOT_FOUND
            ));



}


 public  void  delete( Integer id){
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

 public  List<LigneCommandeClient> findAllLignesCommandesByCommandeClient(Integer idCommande){

        return ligneCommandeClientRepository.findByCommandeClient_Id(idCommande);
 }

 public  CommandeClient updateEtatCommde(Integer idCommande, EtatCommande etatCommande){
        checkIdCommande(idCommande);
     if (!StringUtils.hasLength(String.valueOf(etatCommande))) {
         log.error("L'etat de la commande client is NULL");
         throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un etat null",
                 ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
     }

     CommandeClient commandeClient = checkEtatCommande(idCommande);
     commandeClient.setEtatCommande(etatCommande);

     CommandeClient savedCmdClt= commandeClientRepository.save(commandeClient);
     if (commandeClient.isCommandeLivree()) {
         updateMvtStk(idCommande);
     }
     return  commandeClientRepository.save(savedCmdClt);
 }

 public  CommandeClient updateQuantiteCommande( Integer idCommande, Integer  idLigneCommande , BigDecimal quantite){
     checkIdCommande(idCommande);
     checkIdLigneCommande(idLigneCommande);

     if (quantite == null ||  quantite.compareTo(BigDecimal.ZERO) == 0) {
         log.error("L'ID de la ligne commande is NULL");
         throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une quantite null ou ZERO",
                 ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
     }
     CommandeClient commandeClient = checkEtatCommande(idCommande);

     Optional<LigneCommandeClient> ligneCommadeClientOptional= findLigneCommandeClient(idLigneCommande) ;
     LigneCommandeClient ligneCommandeClient = ligneCommadeClientOptional.get();
     ligneCommandeClient.setQuantite(quantite);
     ligneCommandeClientRepository.save(ligneCommandeClient);

     return  commandeClient;
 }
  public  CommandeClient updateClient( Integer idCommande, Integer idClient){
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


  public  CommandeClient updatArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle){
      checkIdCommande(idCommande);
      checkIdLigneCommande(idLigneCommande);
      checkIdArticle(idArticle, "nouvel");
      CommandeClient commandeClient = checkEtatCommande(idCommande);
      Optional<LigneCommandeClient> ligneCommandeClient= findLigneCommandeClient(idLigneCommande);
      Optional<Article> articleOptional = articleRepository.findById(idArticle);
      if (articleOptional.isEmpty()) {
          throw new EntityNotFoundException(
                  "Aucune article n'a ete trouve avec l'ID " + idArticle, ErrorCodes.ARTICLE_NOT_FOUND);
      }
      List<String> errors= ArticleValidator.validate(articleOptional.get());
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

    private void effectuerSortie(LigneCommandeClient lig) {
        MvtStk mvtStk= new MvtStk();
              mvtStk.setTypeMvt(TypeMvtStk.SORTIE);
              mvtStk.setQuantite(lig.getQuantite());
              mvtStk.setDateMvt(Instant.now());
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

    private void checkIdArticle(Integer idArticle, String msg) {
        if (idArticle == null) {
            log.error("L'ID de " + msg + " is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un " + msg + " ID article null",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
    }

}
