package com.tahiri.gestiondestock.security;



import com.tahiri.gestiondestock.manager.JwtsTokenGenerate;
import com.tahiri.gestiondestock.model.Utilisateur;
import com.tahiri.gestiondestock.service.ApplicationUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter  extends OncePerRequestFilter {
    @Autowired
    private ApplicationUserDetailService applicationUserDetailService;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // request.getRequestURI() => URL
        // on va executer que les request qui commencent par /api
        if (!request.getRequestURI().startsWith("/admin/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // on va executer que les request qui commencent par /api/
        // on doit vérifier que le token existe
        String token = request.getHeader("Authorization");
        try {
            token = JwtsTokenGenerate.readToken(token.substring(7));
        } catch (Exception e) {
            //throw new RuntimeException(e);
            return; // a cause de ce return que tu as une page blanche
        }

        // récupérer l'utilisateur qui contient ce token

        Utilisateur userConnect = this.applicationUserDetailService.findByToken(token.substring(7));
        if (userConnect == null) {
            filterChain.doFilter(request, response);
            return;
        }



        // token et valide => enregistrer l'utilisateur dans le systeme avec ces roles
        Authentication authentication = new UsernamePasswordAuthenticationToken(userConnect, null, userConnect.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
