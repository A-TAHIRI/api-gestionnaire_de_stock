package com.tahiri.gestiondestock.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.fail;

import com.tahiri.gestiondestock.dto.CategorieDto;
import com.tahiri.gestiondestock.exception.EntityNotFoundException;
import com.tahiri.gestiondestock.exception.ErrorCodes;
import com.tahiri.gestiondestock.exception.InvalidEntityException;
import com.tahiri.gestiondestock.model.Categorie;
import com.tahiri.gestiondestock.repository.CategorieRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;


@RunWith(SpringRunner.class)
@SpringBootTest
public  class CategorieServiceTest {
    @Autowired
    private CategorieService categorieService;
    @Autowired
    private CategorieRepository categorieRepository;

    @Test
    public void saveCategoryWithSuccess(){
    Categorie categorie = new Categorie();
         categorie.setCode("cat test");
         categorie.setDesignation("designation test");
         categorie.setIdEntreprise(1);
    Categorie savedCategorie =  categorieService.save(categorie);

    assertNotNull(savedCategorie);
    assertNotNull(savedCategorie.getId());
    assertEquals(categorie.getCode(),savedCategorie.getCode());
    assertEquals(categorie.getIdEntreprise(),savedCategorie.getIdEntreprise());

}
    @Test
    public void updateCategoryWithSuccess(){
        // Créer une catégorie et l'enregistrer dans la base de données
        Categorie categorie = new Categorie();
        categorie.setCode("cat test");
        categorie.setDesignation("designation test");
        categorie.setIdEntreprise(1);
        Categorie savedCategorie = categorieService.save(categorie);

        // Récupérer la catégorie enregistrée à partir de la base de données
        Categorie retrievedCategorie =savedCategorie;

        // Mettre à jour le code de la catégorie
        retrievedCategorie.setCode("cat test update");

        // Enregistrer la catégorie mise à jour dans la base de données
        Categorie updatedCategorie = categorieService.save(retrievedCategorie);

        // Vérifier que la catégorie a été mise à jour correctement
        assertNotNull(updatedCategorie);
        assertEquals(savedCategorie.getId(), updatedCategorie.getId());
        assertEquals(retrievedCategorie.getCode(), updatedCategorie.getCode());
        assertEquals(retrievedCategorie.getDesignation(), updatedCategorie.getDesignation());
        assertEquals(retrievedCategorie.getIdEntreprise(), updatedCategorie.getIdEntreprise());
    }

    @Test
public void shouldThrowInvalidEntityException() {
    Categorie categorie =new Categorie();

    InvalidEntityException expectedException = assertThrows(InvalidEntityException.class, () -> categorieService.save(categorie));

    assertEquals(ErrorCodes.CATEGORY_NOT_VALID, expectedException.getErrorCode());
    assertEquals(1, expectedException.getErrors().size());
    assertEquals("Veuillez renseigner le code de la categorie", expectedException.getErrors().get(0));
}


    @Test
    public void shouldThrowEntityNotFoundException() {
        EntityNotFoundException expectedException = assertThrows(EntityNotFoundException.class, () -> categorieService.getById(0));
        assertEquals(ErrorCodes.CATEGORY_NOT_FOUND, expectedException.getErrorCode());
        assertEquals("Aucune category avec l'ID = 0 n' ete trouve dans la BDD", expectedException.getMessage());
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundException2() {
        categorieService.getById(0);
    }

}