package com.tahiri.gestiondestock.manager;



import com.tahiri.gestiondestock.service.ApplicationUserDetailService;

public class TokenManager {

    /**
     * Cette méthode elle permet de me générer un token unique
     * @param applicationUserDetailService
     * @return token
     */
    public static String generateToken(ApplicationUserDetailService applicationUserDetailService){
        String token;
        do {
            token = AleatoireManager.generateAZC(64);
        }while (applicationUserDetailService.isTokenExiste(token));
        return token;
    }

}
