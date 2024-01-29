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
    private Parcours parcours;
    private Module module;
    private AnneeAcademique anneeAcademique;
    private List<Note> notes;

    public PVModuleResponse(Module module, AnneeAcademique anneeAcademique, Parcours parcours, List<Note> noteList, Double ccSurTrente) {
        this.module = module;
        this.anneeAcademique = anneeAcademique;
        this.parcours = parcours;
        this.notes = noteList;
        this.ccSurTrente = ccSurTrente;
    }
}
