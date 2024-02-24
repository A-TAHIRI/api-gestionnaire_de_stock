package com.tahiri.gestiondestock.repository;

import com.tahiri.gestiondestock.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolesRepository extends JpaRepository<Role , Integer> {

    List<Role> findByRoleNameLikeIgnoreCase(String roleName);

}
