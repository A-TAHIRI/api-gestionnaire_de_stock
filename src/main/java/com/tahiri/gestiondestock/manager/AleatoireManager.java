package com.tahiri.gestiondestock.manager;

import java.util.Random;

public class AleatoireManager {
    /**
     * Génerer une chaine aleatoire 0-9 a-z A-Z _
     * @param length la longeur de la chaine
     * @return
     */
    public static String generateAZ(int length){
        return random("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_", length);
    }

    /**
     * Générer une chaine aleatoire 0-9 a-z A-Z _ &é(§è!çà)$*;.:=+,?
     * @param length la longeur de la chaine
     * @return
     */
    public static String generateAZC(int length){
        return random("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_&é(§è!çà)$*;.:=+,?", length);
    }
    public static String generateaz(int length){
        return random("0123456789abcdefghijklmnopqrstuvwxyz_", length);
    }

    /**
     * Génerer une chaine aleatoire 0-9
     * @param length la longeur de la chaine
     * @return
     */
    public static String generate09(int length){
        return random("0123456789", length);
    }

    /**
     * la fonction qui permet de générer une chaine aleatoire
     * @param chaine
     * @param len
     * @return
     */
    private static String random(String chaine, int len) {
        StringBuilder sb = new StringBuilder(len);
        Random rnd = new Random();
        for (int i = 0; i < len; i++)
            sb.append(chaine.charAt(rnd.nextInt(chaine.length())));
        return sb.toString();
    }
}
