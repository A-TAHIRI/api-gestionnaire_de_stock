package com.tahiri.gestiondestock.service;


import com.tahiri.gestiondestock.dto.ChangerMotDePasseUtilisateurDto;
import com.tahiri.gestiondestock.exception.EntityNotFoundException;
import com.tahiri.gestiondestock.exception.ErrorCodes;
import com.tahiri.gestiondestock.exception.InvalidEntityException;
import com.tahiri.gestiondestock.model.Email;
import com.tahiri.gestiondestock.model.Utilisateur;
import com.tahiri.gestiondestock.repository.UtilisateurRepository;
import io.jsonwebtoken.Claims;
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

    @Autowired
   private JavaMailSender javaMailSender;
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;


    private static final long EXPIRATION_TIME_MS = 300; // Durée de vie du jeton : 5min
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
                .signWith( secretKey(), Jwts.SIG.HS256)
                .compact();
    }


    public  String sendMail(Email email){
        try {

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper= new MimeMessageHelper(mimeMessage,true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(email.getTo());
            if (email.getCc() != null) {
                mimeMessageHelper.setCc(email.getCc());
            }

            mimeMessageHelper.setSubject(email.getSubject());
             String token =generateToken(email.getTo());
             String mail =email.getTo();

            mimeMessageHelper.setText("""
                    `
                                <div>
                                    <p>Vous avez demandé la réinitialisation de votre mot de passe. Veuillez cliquer sur le lien ci-dessous pour procéder à la réinitialisation :</p>
                                    <a href="http://localhost:4200/reset-password?token=%s&mail=%s">Réinitialiser le mot de passe</a>
                                </div>
                            `
                    """  .formatted(token,mail) ,true);


             if (email.getFile() != null){
                 for (int i = 0; i < email.getFile().length; i++) {
                     mimeMessageHelper.addAttachment(
                             email.getFile()[i].getOriginalFilename(),
                             new ByteArrayResource(email.getFile()[i].getBytes()));
                 }

             }

            javaMailSender.send(mimeMessage);

         return "mail send";
        }catch ( Exception e){
            throw new RuntimeException(e);
        }

    }

    public  boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(secretKey()).build().parseSignedClaims(token).getPayload();
            return true;
        } catch (Exception e) {
            return false;
        }


    }


    public  String readToken(String token) {

        Claims claims = Jwts.parser().verifyWith(secretKey()).build().parseSignedClaims(token).getPayload();

        return claims.getSubject();
    }

    public Utilisateur resetPassword(ChangerMotDePasseUtilisateurDto dto , String token){
        boolean valide = this.validateToken(token);
        if (valide == true){
            String email = this.readToken(token);
            Utilisateur utilisateur =this.utilisateurRepository.findByEmail(email).orElseThrow( ()-> new EntityNotFoundException("Aucun utilisateur n'a ete trouve avec l'ID " + email, ErrorCodes.UTILISATEUR_NOT_FOUND));
            dto.setId(utilisateur.getId());
            utilisateur.setMdp(passwordEncoder.encode(dto.getMotDePasse()));

         return  utilisateurRepository.save(utilisateur);

        }

       return null;

    }

}
