package com.tahiri.gestiondestock.controller;


import com.tahiri.gestiondestock.model.FileUploadInfo;
import com.tahiri.gestiondestock.service.ArticleService;
import com.tahiri.gestiondestock.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@RestController
public class FileController {

    @Autowired
    private PhotoService photoService;
    @Autowired
    private ArticleService articleService;


    @Value("${file.upload.path}")
    private String pathUploadFile;




    /**
     * Cette méthode elle sera executer lorsque l'utilisateur va uploder un fichier
     * @param file fichier récupérer du JS
     */
    @PostMapping("/file/upload")
    public FileUploadInfo upload(@RequestParam MultipartFile file){
        /*if (id == null || "0".equals(id)){
            return new FileUploadInfo(true, "Aucun bien n'est associer", null);
        }*/
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd/");
        String pathForDate = dateFormat.format(new java.util.Date());

        File directory = new File(pathUploadFile+pathForDate);

        // vérifier si le répertoire n'existe pas on va le créer avec la commande mkdir
        if (!directory.exists()){
            directory.mkdirs(); // pour le créer
        }


        // file.getOriginalFilename()) => nom d'origine du fichier a uploader
        try{
            String fileName = file.getOriginalFilename(); // mon.Image1.png ou monImage1.webp
            int positionPoint = fileName.lastIndexOf(".");
            // copier le fichier du répertoire tmp vers le repertoire upload
            String extensions = fileName.substring(positionPoint); // ".png"
            List<String> extAutoriser = List.of(".png",".jpg",".webp",".svg",".jpeg");
            if (!extAutoriser.contains(extensions.toLowerCase())){ // toLowerCase() => miniscule
                return new FileUploadInfo(true, "Extensions non autoriser", null);
            }

            String uniqueId = UUID.randomUUID().toString();
            fileName = uniqueId + extensions;
            file.transferTo(new File(directory.getAbsolutePath(), fileName));



            return new FileUploadInfo(false, null, pathForDate+fileName);
        }catch (Exception ex){
            //ex.printStackTrace();
            return new FileUploadInfo(true, ex.getMessage(), null);
        }
    }


    /**
     * Cette méthode et appeler l'orsqu'on veux afficher une image
     * @param fileName nom de l'image
     * @return ResponseEntity qui va contenir l'image sous forme de byte
     */
    @GetMapping("/file/image/{annee}/{mois}/{jour}/{fileName}")
    public ResponseEntity<?> displayImage(@PathVariable String fileName, @PathVariable String annee, @PathVariable String mois, @PathVariable String jour){
        // pathUploadFile => Chemin que l'image et sauvegarder
        File file = new File(pathUploadFile+annee+"/"+mois+"/"+jour+"/", fileName);
        if (!file.exists()){
            return null;
        }
        try{
            byte[] imageData = Files.readAllBytes(file.toPath());
            return ResponseEntity.ok().contentType(MediaType.valueOf("image/png")).body(imageData);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }

    }



}
