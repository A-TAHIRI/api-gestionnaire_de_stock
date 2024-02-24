package com.tahiri.gestiondestock.manager;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Calendar;

/**
 * Cette classe va nous permettre de générer un token et récupérer sa valeur
 */
public class JwtsTokenGenerate {

    /**
     * Methode qui va nous permettre de generer un token <br>
     * elle permet de crypter l'id user
     * @param token token du user (identifiant)
     * @return
     */
    public static String generateToken(String token){

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 24);

        return Jwts.builder()
                .subject(token)
                .expiration(calendar.getTime())
                .signWith(secretKey(), Jwts.SIG.HS256)
                .compact();

    }

    /**
     * Cette methode va nous permettre de lire le token et récupérer l'id user
     * @param token
     * @return l'id user
     * @throws Exception si le token n'est pas valide
     */
    public static String readToken(String token) throws Exception{

        Claims claims = Jwts.parser().verifyWith(secretKey()).build().parseSignedClaims(token).getPayload();

        return claims.getSubject();
    }

    private static SecretKey secretKey() {
        String secretString = "wzUpGa9k4LTV3QHuY8qVrt6wOENkvdes5vLHVc1ex6581IiQ";
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));
    }

}
