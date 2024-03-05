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
public class PVEtudiant {

    private String matricule;
    private String nom;
    private String lieuDeNaissance;
    private Date dateDeNaissance;

    List<CoursFond> coursFondList;
    private Double moyGeneFond;
    private int creditFond;
    private Double mgpFond;

    List<CoursComp> coursCompList;
    private Double moyGeneComp;
    private int creditComp;
    private Double mgpComp;

    List<CoursPro> coursProList;
    private Double moyGenePro;
    private int creditPro;
    private Double mgpPro;

    private Double moyGene;
    private Double mgp;
    private int credit;
    private String decision;
}
