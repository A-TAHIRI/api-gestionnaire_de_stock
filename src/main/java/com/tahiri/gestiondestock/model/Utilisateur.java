package com.tahiri.gestiondestock.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity

@Data

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Utilisateur")

public class Utilisateur extends  AbstractEntity  implements UserDetails {
    @Column(name = "nom")
    private  String nom;

    @Column(name = "prenom")
    private  String prenom;

    @Embedded
    private  Adresse adresse;

    @Column(name = "photo")
    private  String photo;

    @Column(name = "email")
    private  String email;

    @Column(name = "numTel")
    private  String numTel;

    @Column(name = "datedenaissance")
    private Date dateDeNaissance;

    @Column(name = "mdp")
    private String mdp;

    private String token;

    private boolean active; // actif ou pas

    @ManyToOne
    @JoinColumn(name = "identreprise")
    private  Entreprise entreprise;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Photo> photos;

    public boolean isRole(String roleName){
        //return this.roles.stream().anyMatch(role -> role.getNom().equals(roleName));
        if (this.roles == null) {
            return false;
        }
        for (Role role : roles) {
            if (role.getRoleName().equals(roleName)) {
                return true;
            }
        }
        return false;
    }

/****** methode userDetail*******/

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getPassword() {
        return this.mdp;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.active;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }

}
