package com.api.gestnotesapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PVCoursSansEEResponse {

    //    Parcours
    private String label;

    //    Cours
    private String code;
    private String intitule;
    int credit;

    //    Annee academique
    private String anneeAcademique;

    //    Note
    private List<PVEtudiantModule> pvEtudiantModuleList;

}
