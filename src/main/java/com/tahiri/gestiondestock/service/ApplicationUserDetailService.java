package com.tahiri.gestiondestock.service;


import com.tahiri.gestiondestock.model.Utilisateur;
import com.tahiri.gestiondestock.repository.UtilisateurRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class ApplicationUserDetailService  implements UserDetailsService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;


    public Utilisateur findByToken(String token){
        List<Utilisateur> users = utilisateurRepository.findByToken(token);
        if (users.size() != 1){
            return null;
        }
        return users.get(0);
    }



    public void saveToken(Integer id, String token){
        utilisateurRepository.updateTokenById(token, id);
    }

    public boolean isTokenExiste(String token){
        return utilisateurRepository.countByTokenLike(token) > 0;
    }



    /**
     * Récupérer l'utilisateur à partir de son email
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Utilisateur userDetails =  utilisateurRepository.findByEmailIgnoreCase(username);
        if (userDetails == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return userDetails;
    }


}
