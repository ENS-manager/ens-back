package com.api.gestnotesapi.dto;

import com.api.gestnotesapi.entities.AnneeAcademique;
import com.api.gestnotesapi.entities.Module;
import com.api.gestnotesapi.entities.Note;
import com.api.gestnotesapi.entities.Parcours;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PVModuleResponse {

    private Double ccSurTrente;

//    Etudiant
    private String matricule;
    private String nom;

//    Parcours
    private String label;

//    Module
    private String code;
    private String intitule;
    int credit;

//    Annee academique
    private String anneeAcademique;

//    Note
    private List<NoteDto> notes;

    public PVModuleResponse(String code, String intitule, String matricule, int credit, String nom, String anneeAcademique, String label, List<NoteDto> notes, Double ccSurTrente) {
        this.anneeAcademique = anneeAcademique;
        this.notes = notes;
        this.ccSurTrente = ccSurTrente;
        this.code = code;
        this.intitule = intitule;
        this.label = label;
        this.matricule = matricule;
        this.nom = nom;
        this.credit = credit;
    }
}
