package com.tahiri.gestiondestock.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data

@NoArgsConstructor
@AllArgsConstructor
public class Email {

  private  String to;
    private String body;
    private String subject;
    private String[] cc;
    /*
    private MultipartFile[] file;
    */

}
