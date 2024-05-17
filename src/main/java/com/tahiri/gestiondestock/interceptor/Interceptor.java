package com.tahiri.gestiondestock.interceptor;


import com.tahiri.gestiondestock.dto.AuthRequestDto;
import com.tahiri.gestiondestock.model.Utilisateur;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.slf4j.MDC;

import java.util.Collection;


public class Interceptor implements StatementInspector {

    private static final Logger logger = LoggerFactory.getLogger(Interceptor.class);


    @Override
    public String inspect(String sql) {

        logger.info("sql avant"+sql);
        // Validez que le SQL commence par "select"
        if (StringUtils.hasLength(sql) && sql.trim().toLowerCase().startsWith("select")) {
            int startIndex = sql.trim().toLowerCase().indexOf("select") + 7;
            int endIndex = sql.indexOf(".", startIndex);

            // Validez que les indices sont corrects
            if (endIndex > startIndex) {
                String entityName = sql.substring(startIndex, endIndex).trim(); // Récupérer le nom de l'entité


                // Récupérer `idEntreprise` du MDC
                String idEntreprise = MDC.get("idEntreprise");
logger.info("identreprise"+idEntreprise);
                String lowerCaseSql = sql.toLowerCase();
                String lowerCaseEntity = entityName.toLowerCase();
                int indexWhere = lowerCaseSql.indexOf("where");
                int indexLimit = lowerCaseSql.indexOf("limit");
                int indexLike = lowerCaseSql.indexOf("like");
                String condition = " and " + entityName + ".identreprise = " + idEntreprise;


                if (lowerCaseSql.contains(lowerCaseEntity + ".identreprise")) {
                    // Si token n'est pas présent
                    if (!lowerCaseSql.contains("where " + lowerCaseEntity + ".token") && !lowerCaseSql.contains("upper") ) {

                        // Ajouter la condition à la bonne position
                        if (indexWhere != -1) {
                            if (indexLimit != -1) {
                                // Si LIMIT est présent, ajouter avant
                                sql = sql.substring(0, indexLimit) + condition + " " + sql.substring(indexLimit);
                            } else if (indexLike != -1) {
                                // Si LIKE est présent, ajouter après le LIKE
                                sql = sql.substring(0, indexLike + 4) + condition + " " + sql.substring(indexLike + 4);
                            } else {
                                // Ajouter après WHERE
                                sql = sql + condition;
                            }
                        } else {
                            // Si WHERE n'existe pas, créer avec la condition
                            sql = sql + " where "   + entityName + ".identreprise = " + idEntreprise;;
                        }

                    }


                }


            }
        }
System.out.println("sql apres"+sql);


        return sql;
    }


}



