package com.api.gestnotesapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PVEtudiant {

    private String matricule;
    private String nom;
    private String lieuDeNaissance;
    private LocalDate dateDeNaissance;
    private Double moyGene;
    private Double moyCoursFond;
    private int creditCoursFond;
    private Double moyCoursComp;
    private int creditCoursComp;
    private Double moyCoursPro;
    private int creditCoursPro;

    private Double mgp;
    private int credit;
    private String decision;

    private List<CoursDto> coursDtoList;
}
