package com.api.gestnotesapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PVEtudiantModule {

    private String matricule;
    private String nom;
    private Double ccSurTrente;
    private List<NoteDto> notes;
}
