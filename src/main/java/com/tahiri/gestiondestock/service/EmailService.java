package com.tahiri.gestiondestock.service;


import com.tahiri.gestiondestock.model.Email;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
   private JavaMailSender javaMailSender;

    public  String sendMail(Email email){
        try {

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper= new MimeMessageHelper(mimeMessage,true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(email.getTo());
            mimeMessageHelper.setCc(email.getCc());
            mimeMessageHelper.setSubject(email.getSubject());
            mimeMessageHelper.setText(email.getBody());
            /*

            for (int i = 0; i < email.getFile().length; i++) {
                mimeMessageHelper.addAttachment(
                        email.getFile()[i].getOriginalFilename(),
                        new ByteArrayResource(email.getFile()[i].getBytes()));
            }
*/
            javaMailSender.send(mimeMessage);

         return "mail send";
        }catch ( Exception e){
            throw new RuntimeException(e);
        }

    }

}
