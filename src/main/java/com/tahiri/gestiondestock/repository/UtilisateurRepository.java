package com.tahiri.gestiondestock.repository;


import com.tahiri.gestiondestock.model.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;


@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {



    Utilisateur findByEmailIgnoreCase(String email);


    @Transactional
    @Modifying
    @Query("update Utilisateur u set u.token = ?1 where u.id = ?2")
    int updateTokenById(String token, Integer id);

    long countByTokenLike(String token);

    List<Utilisateur> findByToken(String token);

  Optional < Utilisateur> findByEmail(String email);
    Page<Utilisateur> findByNomContaining(String nom, Pageable pageable);

}
