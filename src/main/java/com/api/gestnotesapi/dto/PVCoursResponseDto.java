package com.api.gestnotesapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PVCoursResponseDto {

    private String matricule;
    private String nom;
    private Double moyenneSurVingt;
    private Double mgp;
    private Double ccSurTrente;
    private Double moyenneSurCent;
    private String decision;
    private String grade;
    private List<NoteDto> notes;
}
