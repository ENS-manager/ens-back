package com.api.gestnotesapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Diplome {

//    Departement
    private String departementFrench;
    private String departementEnglish;

//    Etudiant
    private String nom;
    private String matricule;
    private String lieuDeNaissance;
    private Date dateDeNaissance;

//    Option
    private String optionFrench;
}
