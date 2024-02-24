package com.tahiri.gestiondestock.controller;

import com.tahiri.gestiondestock.dto.ClientDto;
import com.tahiri.gestiondestock.dto.MvtStkDto;
import com.tahiri.gestiondestock.model.Client;
import com.tahiri.gestiondestock.model.MvtStk;
import com.tahiri.gestiondestock.service.MvtStkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/mvtstk")
@CrossOrigin(origins = {"http://localhost:4200","https://monsite.fr"})
public class MvtStkController {



    @Autowired

    private MvtStkService mvtStkService;

   


}
