package com.tahiri.gestiondestock.service;


import com.tahiri.gestiondestock.model.Photo;
import com.tahiri.gestiondestock.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;
    public Photo add(Photo photo){
        return this.photoRepository.save(photo);
    }
}
