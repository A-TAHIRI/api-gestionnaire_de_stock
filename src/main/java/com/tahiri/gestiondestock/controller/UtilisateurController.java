package com.tahiri.gestiondestock.controller;


import com.tahiri.gestiondestock.dto.ChangerMotDePasseUtilisateurDto;
import com.tahiri.gestiondestock.dto.UtilisateurDto;
import com.tahiri.gestiondestock.model.HttpResponse;
import com.tahiri.gestiondestock.model.Utilisateur;
import com.tahiri.gestiondestock.repository.RolesRepository;
import com.tahiri.gestiondestock.service.RoleService;
import com.tahiri.gestiondestock.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tahiri.gestiondestock.utils.constant.UTILISATEUR_ENDPOINT;
import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

@RestController

public class UtilisateurController {
    @Autowired
    private UtilisateurService utilisateurService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RolesRepository rolesRepository;

    /**
     * enregistrer un utilisateur dans la bdd
     *
     * @param utilisateur
     * @return
     */
    @PostMapping("/api/v1/utilisateurs")
    public Utilisateur enregistrer(@RequestBody Utilisateur utilisateur) {
        return utilisateurService.save(utilisateur);
    }


    /**
     * recupirer tous les utilisateur
     *
     * @return
     */
/*
    @GetMapping("")
    public List<UtilisateurDto> tousUtilisateurs() {
        List<Utilisateur> utilisateurs = utilisateurService.getdAll();
        List<UtilisateurDto> utilisateurDtos = new ArrayList<>();
        for (Utilisateur u : utilisateurs) {

            utilisateurDtos.add(new UtilisateurDto(u));
        }
        return utilisateurDtos;
    }
**/
    /**
     * recupirer l'utilisateur de l'id
     *
     * @param id
     * @return un utilisateur
     */
    @GetMapping("/api/v1/utilisateurs/{id}")
    public UtilisateurDto unUtilisateur(@PathVariable Integer id) {
        return new UtilisateurDto(utilisateurService.getById(id));
    }

    /**
     *
     * @param email
     * @return
     */
    /*
    @GetMapping("/email/{email}")
    public UtilisateurDto utilisateurByEmail(@PathVariable String email){
        return  new UtilisateurDto(utilisateurService.getByEmail(email)) ;

    }
    */




    /**
     * supprimer l'utilisateur par id
     *
     * @param id
     */

    @DeleteMapping("/api/v1/utilisateurs/{id}")
    public void supprimer(@PathVariable Integer id) {
        utilisateurService.delete(id);

    }

    /**
     * modifier l'utilisateur
     *
     * @param id
     * @param utilisateur
     * @return
     */
    @PutMapping("/api/v1/utilisateurs/{id}")
    public UtilisateurDto modifier(@PathVariable Integer id, @RequestBody Utilisateur utilisateur) {
        Utilisateur old = utilisateurService.getById(id);
        if (old != null) {
            utilisateur.setId(id);

            return new UtilisateurDto(utilisateurService.save(utilisateur));
        }
        return new UtilisateurDto(utilisateurService.save(utilisateur));
    }

    /**
     * Modifier password
     * @param dto
     * @return
     */
    @PutMapping("/update/password")
    Utilisateur changerMotDePasse(@RequestBody ChangerMotDePasseUtilisateurDto dto){

        return utilisateurService.changerMotDePasse(dto)  ;
    }

    /*
    @PostMapping("/addrole/{id}")
    public  void addrole(@PathVariable Integer id , @RequestBody Role role){
        Utilisateur utilisateur = utilisateurService.getById(id);

        if (utilisateur != null){
            role.setUtilisateur(utilisateur);
            roleService.save(role);
        }



    }
*/


/*
    @GetMapping("")
    public ResponseEntity<HttpResponse> getUsers(@RequestParam Optional<String> name,
                                                 @RequestParam Optional<Integer> page,
                                                 @RequestParam Optional<Integer> size) throws InterruptedException {
       // TimeUnit.SECONDS.sleep(3);
        //throw new RuntimeException("Forced exception for testing");
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("page", utilisateurService.getUsers(name.orElse(""), page.orElse(0), size.orElse(10))))
                        .message("Users Retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());

    }
*/

    /**
     * récuper les utilisateur par page (pagination)
     * @param name
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/api/v1/utilisateurs")
    public Page<UtilisateurDto> getUsers(@RequestParam(defaultValue = "",required = false,name = "nom") String name,
                                      @RequestParam Optional<Integer> page,
                                      @RequestParam Optional<Integer> size){

       Page<Utilisateur>   utilisateurPage  =   this.utilisateurService.getUsers(name,page.orElse(0),size.orElse(10));

       Page<UtilisateurDto> utilisateurDtoPage =utilisateurPage.map(utilisateur -> {
           UtilisateurDto utilisateurDto =new UtilisateurDto(utilisateur);
           return utilisateurDto;
       });
       return utilisateurDtoPage;

    }

    /**
     * retourn le nombre des utilisateur dans le mois president
     * @return
     */

    @GetMapping("/api/v1/utilisateurs/bymonth")
    public int getByMonth(){
        return utilisateurService.countUtilisateurBymouth();
    }
    /**
     * retourn le nombre des utilisateur dans le mois president
     * @return
     */

    @GetMapping("/api/v1/utilisateurs/bythismonth")
    public int getByThisMonth(){
        return utilisateurService.countUtilisateurByThisMouth();
    }

    /**
     * retourn le nombre des utilisateur dans l'anné actuel
     * @return
     */

    @GetMapping("/api/v1/utilisateurs/byyear")
    public int getByYear(){
        return utilisateurService.countUtilisateurByYear();
    }
    /**
     * retourn le nombre des utilisateur dans l'anné président
     * @return
     */

    @GetMapping("/api/v1/utilisateurs/bylastyear")
    public int getByLastYear(){
        return utilisateurService.countUtilisateurByLastYear();
    }


    /**
     * retourn le nombre des utilisateur le jour actuelle
     * @return
     */

    @GetMapping("/api/v1/utilisateurs/byday")
    public int getByDay(){
        return utilisateurService.countUtilisateurByDay();
    }
    /**
     * retourn le nombre des utilisateur le jour président
     * @return
     */

    @GetMapping("/api/v1/utilisateurs/bylastday")
    public int getByLastDay(){
        return utilisateurService.countUtilisateurByLastDay();
    }

}
