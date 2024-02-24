package com.tahiri.gestiondestock.repository;


import com.tahiri.gestiondestock.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo , Integer> {
}
