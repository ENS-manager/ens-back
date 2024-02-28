package com.api.gestnotesapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private LocalDate dateDeNaissance;

//    Option
    private String optionFrench;
}
