package com.api.gestnotesapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PVCoursSansEEResponse {

    private Double ccSurTrente;

    //    Etudiant
    private String matricule;
    private String nom;

    //    Parcours
    private String label;

    //    Cours
    private String code;
    private String intitule;
    int credit;

    //    Annee academique
    private String anneeAcademique;

    //    Note
    private List<NoteDto> notes;

    public PVCoursSansEEResponse(Double ccSurTrente, String matricule, String nom, String label, String code, String intitule, int credit, String anneeAcademique, List<NoteDto> notes) {
        this.ccSurTrente = ccSurTrente;
        this.matricule = matricule;
        this.nom = nom;
        this.label = label;
        this.code = code;
        this.intitule = intitule;
        this.credit = credit;
        this.anneeAcademique = anneeAcademique;
        this.notes = notes;
    }
}
