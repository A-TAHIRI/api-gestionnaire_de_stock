package com.tahiri.gestiondestock.controller;


import com.tahiri.gestiondestock.model.Email;
import com.tahiri.gestiondestock.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/email")
public class EmailSendController {
    @Autowired
    private EmailService emailService;
    @PostMapping("/send")
    public String sendMail(@RequestBody Email email) {
        return emailService.sendMail(email);
    }

}
