package com.tahiri.gestiondestock.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import lombok.*;

@Entity

@Data

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Role")

public class Role extends  AbstractEntity   implements GrantedAuthority {

    @Column(name = "roleName")
     private  String roleName;



    /*********** GrantedAuthority *******/

    @Override
    public String getAuthority() {
        return this.roleName;
    }
}


