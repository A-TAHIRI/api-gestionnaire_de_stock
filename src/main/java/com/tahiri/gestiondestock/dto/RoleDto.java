package com.tahiri.gestiondestock.dto;





import com.tahiri.gestiondestock.model.Role;

import com.tahiri.gestiondestock.model.Utilisateur;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;


@Data

public class RoleDto {

    private  Integer id;
    private  String roleName;
    
public  RoleDto (Role role) {
    this.id=role.getId();
    this.roleName=role.getRoleName();


}

}
