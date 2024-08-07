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
public class EmailSendController {
    @Autowired
    private EmailService emailService;
    @Autowired
    UtilisateurService utilisateurService;

    @PostMapping("/email/send")
    public void sendMail(@RequestBody Email email) {

         emailService.sendMail(email);
    }


    @PutMapping("/reset-password")
    public Utilisateur resetPasswor(@RequestBody ChangerMotDePasseUtilisateurDto dto, @RequestParam("token") String token) throws Exception {
        return this.emailService.resetPassword(dto, token);
    }

    @GetMapping("/email/validToken")
    public boolean valide(@RequestBody String token) {
        return this.emailService.validateToken(token);
    }


}
