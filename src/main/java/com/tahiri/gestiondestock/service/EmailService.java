package com.tahiri.gestiondestock.service;


import com.tahiri.gestiondestock.dto.ChangerMotDePasseUtilisateurDto;
import com.tahiri.gestiondestock.exception.EntityNotFoundException;
import com.tahiri.gestiondestock.exception.ErrorCodes;
import com.tahiri.gestiondestock.exception.InvalidEntityException;
import com.tahiri.gestiondestock.manager.JwtsTokenGenerate;
import com.tahiri.gestiondestock.model.Email;
import com.tahiri.gestiondestock.model.Utilisateur;
import com.tahiri.gestiondestock.repository.UtilisateurRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    private String extraitEmail;
    private String token;

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private ApplicationUserDetailService applicationUserDetailService;


    private static final long EXPIRATION_TIME_MS = 36000; // Durée de vie du jeton

    private static SecretKey secretKey() {
        String secretString = "wzUpGa9k4LTV3QHuY8qVrt6wOENkvdes5vLHVc1ex6581IiQ";
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));
    }


    public static String generateToken(String userEmail) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME_MS);

        return Jwts.builder()
                .setSubject(userEmail)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey(), Jwts.SIG.HS256)
                .compact();

    }


    public void sendMail(Email email) {
        this.extraitEmail = email.getTo();

        try {

            Utilisateur utilisateur = this.utilisateurRepository.findByEmail(this.extraitEmail).orElseThrow(()-> new EntityNotFoundException("aucun utilisateur associer à cet Email: " + email.getTo() ));
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(email.getTo());
            if (email.getCc() != null) {
                mimeMessageHelper.setCc(email.getCc());
            }

            mimeMessageHelper.setSubject(email.getSubject());
            this.token = generateToken(email.getTo());
            this.modifierToken();


            mimeMessageHelper.setText("""
                    `
                                <div>
                                    <p>Vous avez demandé la réinitialisation de votre mot de passe. Veuillez cliquer sur le lien ci-dessous pour procéder à la réinitialisation :</p>
                                    <a href="http://localhost:4200/reset-password?token=%s">Réinitialiser le mot de passe</a>
                                </div>
                            `
                    """.formatted(token), true);


            if (email.getFile() != null) {
                for (int i = 0; i < email.getFile().length; i++) {
                    mimeMessageHelper.addAttachment(
                            email.getFile()[i].getOriginalFilename(),
                            new ByteArrayResource(email.getFile()[i].getBytes()));
                }

            }

            javaMailSender.send(mimeMessage);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Utilisateur modifierToken() {

        // Recherche de l'utilisateur par email
        Utilisateur utilisateur = this.utilisateurRepository.findByEmail(this.extraitEmail).orElseThrow(()-> new EntityNotFoundException("aucun utilisateur associer à cet Email: " + this.extraitEmail
        ) );

        // Vérification si l'utilisateur existe
        if (utilisateur != null) {

            // Modification du jeton de l'utilisateur
            utilisateur.setToken(this.token);

            // Enregistrement des modifications dans la base de données
            return utilisateurRepository.save(utilisateur);
        } else {
            return null;
        }
    }


    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(secretKey()).build().parseSignedClaims(token).getPayload();
            return true;
        } catch (Exception e) {
            return false;
        }


    }


    public String readToken(String token) {
        Claims claims = Jwts.parser().verifyWith(secretKey()).build().parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }

    public Utilisateur resetPassword(ChangerMotDePasseUtilisateurDto dto, String token) throws Exception {

       // if (validateToken(token)) {throw new Exception("Le token a expiré");}
            Utilisateur utilisateur = this.utilisateurRepository.findByToken(token).get(0);
            if (utilisateur != null){
                dto.setId(utilisateur.getId());
                utilisateur.setMdp(passwordEncoder.encode(dto.getMotDePasse()));
                return utilisateurRepository.save(utilisateur);
            }else{
                throw new Exception("Aucun utilisateur trouvé avec le token spécifié");
            }






    }

}
