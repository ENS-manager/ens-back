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
    private Parcours parcours;
    private Cours cours;
    private AnneeAcademique anneeAcademique;
    private List<Note> notes;

    public PVCoursResponse(Cours cours, AnneeAcademique anneeAcademique, Parcours parcours, List<Note> noteList, Double ccSurTrente, Double moyenneSurCent, Double moyenneSurVingt, String decision, Double mgp, String grade) {
        this.cours = cours;
        this.anneeAcademique = anneeAcademique;
        this.parcours = parcours;
        this.notes = noteList;
        this.ccSurTrente = ccSurTrente;
        this.moyenneSurCent = moyenneSurCent;
        this.moyenneSurVingt = moyenneSurVingt;
        this.decision = decision;
        this.mgp = mgp;
        this.grade = grade;
    }
}
