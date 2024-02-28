package com.api.gestnotesapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PVModuleResponse {

//    Parcours
    private String label;

//    Module
    private String code;
    private String intitule;
    int credit;

//    Annee academique
    private String anneeAcademique;

    List<PVEtudiantModule> pvEtudiantModuleList;

}
