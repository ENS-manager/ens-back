package com.api.gestnotesapi.dto;

import com.api.gestnotesapi.entities.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PVCoursResponse {

    private Double moyenneSurVingt;
    private Double mgp;
    private Double ccSurTrente;
    private Double moyenneSurCent;
    private String decision;
    private String grade;
    int session;
    private String matricule;
    private String nom;
    private String label;
    private String code;
    private String intitule;
    private String anneeAcademique;
    private List<Note> notes;

    public PVCoursResponse(int session, String matricule, String nom, String label, String code, String intitule, String anneeAcademique, List<Note> noteList, Double ccSurTrente, Double moyenneSurCent, Double moyenneSurVingt, String decision, Double mgp, String grade) {
        this.anneeAcademique = anneeAcademique;
        this.session = session;
        this.notes = noteList;
        this.ccSurTrente = ccSurTrente;
        this.moyenneSurCent = moyenneSurCent;
        this.moyenneSurVingt = moyenneSurVingt;
        this.decision = decision;
        this.mgp = mgp;
        this.grade = grade;
        this.code = code;
        this.intitule = intitule;
        this.label = label;
        this.matricule = matricule;
        this.nom = nom;
    }
}
