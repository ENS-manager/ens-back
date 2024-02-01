package com.api.gestnotesapi.dto;

import com.api.gestnotesapi.entities.Etudiant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PVGrandJuryResponse {
    Etudiant etudiant;
    Double noteStage;
    Double moyenneEcrit;
    String moyenneGene;
    Double noteStagePra;
    String decision;
    String mention;
    MoyenneByTypeCours moyenneByTypeCours;
    List<MoyenneCours> moyenneCours;
}
