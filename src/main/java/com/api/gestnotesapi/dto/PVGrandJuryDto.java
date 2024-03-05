package com.api.gestnotesapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PVGrandJuryDto {
    String matricule;
    String nom;
    Date dateDeNaissance;
    String lieuDeNaissance;
    Double moyenneEcrite;
    Double moyenneGene;
    Double noteStagePra;
    Double noteStage;
    Double complementaire;
    Double fondamentale;
    Double professionnelle;
    String decision;
    String admissibilite;
    String mention;
    List<MoyenneCours> moyenneCours;
}
