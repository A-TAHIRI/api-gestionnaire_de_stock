package com.tahiri.gestiondestock.security;



import com.tahiri.gestiondestock.interceptor.Interceptor;
import com.tahiri.gestiondestock.manager.JwtsTokenGenerate;
import com.tahiri.gestiondestock.model.Utilisateur;
import com.tahiri.gestiondestock.service.ApplicationUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class SecurityFilter  extends OncePerRequestFilter {
    @Autowired
    private ApplicationUserDetailService applicationUserDetailService;

    private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // request.getRequestURI() => URL
        // on va executer que les request qui commencent par /api
        if (!request.getRequestURI().startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // on va executer que les request qui commencent par /api/
        // on doit vérifier que le token existe
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        try {
         token = JwtsTokenGenerate.readToken(token);
        } catch (Exception e) {
           // throw new RuntimeException(e);
            logger.error("Erreur lors de la lecture du token", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalide");
            return; // a cause de ce return que tu as une page blanche
        }

        // récupérer l'utilisateur qui contient ce token

        Utilisateur userConnect = this.applicationUserDetailService.findByToken(token);
        if (userConnect == null) {
            filterChain.doFilter(request, response);
            return;
        }



        // Si `userConnect` n'est pas `null`, obtenir `idEntreprise` et le stocker dans le MDC

        if (userConnect != null && userConnect.getEntreprise() != null) {
            String email =userConnect.getEmail();
            String idEntreprise = userConnect.getEntreprise().getId().toString();
            MDC.put("idEntreprise", idEntreprise);
        }else {
            logger.info("idEntreprise est null");
        }
        // token et valide => enregistrer l'utilisateur dans le systeme avec ces roles
        Authentication authentication = new UsernamePasswordAuthenticationToken(userConnect, null, userConnect.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
