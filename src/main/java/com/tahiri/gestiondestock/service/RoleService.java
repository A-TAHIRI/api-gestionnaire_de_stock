package com.tahiri.gestiondestock.service;


import com.tahiri.gestiondestock.model.Role;
import com.tahiri.gestiondestock.repository.RolesRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class RoleService {

    @Autowired
    private RolesRepository rolesRepository;


    public  Role save(Role role){
        return  rolesRepository.save(role);
    }


    // Ajouter un role
    public Role addRole(String nom) {
        // avant d'ajouter un role on doit vérifier si le role existe deja

        List<Role> role = rolesRepository.findByRoleNameLikeIgnoreCase(nom);
        if (role.size() >= 1) {
            return role.get(0);
        }

        // si le role n'existe pas je vais le créer
        Role newRole = new Role();
        newRole.setRoleName(nom);
        return rolesRepository.save(newRole);
    }
}
