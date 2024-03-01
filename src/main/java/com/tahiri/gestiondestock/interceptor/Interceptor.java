package com.tahiri.gestiondestock.interceptor;


import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.util.StringUtils;

public class Interceptor implements StatementInspector {
    @Override


    public  String inspect(String sql ){
        if (StringUtils.hasLength(sql) && sql.toLowerCase().startsWith("select")) {

            if (sql.contains("where")) {
                sql = sql + " and idEntreprise = 7" ;
            } else {
                sql = sql + " where idEntreprise = 7" ;
            }
        }
        return sql;
    }


}
