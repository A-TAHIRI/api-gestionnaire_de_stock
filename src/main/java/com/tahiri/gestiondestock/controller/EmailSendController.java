package com.tahiri.gestiondestock.controller;


import com.tahiri.gestiondestock.dto.ChangerMotDePasseUtilisateurDto;
import com.tahiri.gestiondestock.model.Email;
import com.tahiri.gestiondestock.model.Utilisateur;
import com.tahiri.gestiondestock.service.EmailService;
import com.tahiri.gestiondestock.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://monsite.fr"})
public class EmailSendController {
    @Autowired
    private EmailService emailService;
    @Autowired
    UtilisateurService utilisateurService;

    @PostMapping("/email/send")
    public String sendMail(@RequestBody Email email) {
        return emailService.sendMail(email);
    }


    @PutMapping("/reset-password")
    public Utilisateur resetPasswor(@RequestBody ChangerMotDePasseUtilisateurDto dto, @RequestBody String token) {
        return this.emailService.resetPassword(dto, token);
    }

    @GetMapping("/email/validToken")
    public boolean valide(@RequestBody String token) {
        return this.emailService.validateToken(token);
    }


}
