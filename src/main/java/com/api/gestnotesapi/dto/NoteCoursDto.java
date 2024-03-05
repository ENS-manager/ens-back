package com.api.gestnotesapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteCoursDto {
    private Long id;
    private String nom;
    private String matricule;
    private Double valeur;
}
