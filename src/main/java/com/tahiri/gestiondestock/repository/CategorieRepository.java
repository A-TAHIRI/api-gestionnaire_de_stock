package com.tahiri.gestiondestock.repository;


import com.tahiri.gestiondestock.model.Categorie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Integer> {
    Optional<Categorie> findByCode(String code);
    Page<Categorie> findByDesignationContaining(String name, Pageable pageable);
}
