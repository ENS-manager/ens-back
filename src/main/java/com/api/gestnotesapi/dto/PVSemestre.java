package com.api.gestnotesapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PVSemestre {

    private String anneeAca;
    private String option;
    private int niveau;
    List<CoursMoyClasse> coursMoyClasseList;
    List<PVEtudiant> pvEtudiantList;
    private Stats statsList;
    private int effectif;
}
