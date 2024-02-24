package com.tahiri.gestiondestock.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileUploadInfo {

    private boolean erreur;
    private String message;
    private String pathFile;



}
