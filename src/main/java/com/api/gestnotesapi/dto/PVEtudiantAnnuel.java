package com.api.gestnotesapi.dto;

import com.api.gestnotesapi.entities.TYPE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PVEtudiantAnnuel {

    private String matricule;
    private String nom;
    private TYPE type;
    private Double moySem1;
    private Integer creditSem1;
    private Double moySem2;
    private Integer creditSem2;
    private Double moyAnnuelle;
    private Double mgp;
    private String decision;
    private List<MoyenneCours> moyenneCoursSem1;
    private List<MoyenneCours> moyenneCoursSem2;
}
