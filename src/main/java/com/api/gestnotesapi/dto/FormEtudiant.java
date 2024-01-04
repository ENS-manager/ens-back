/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.gestnotesapi.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 *
 * @author COMPUTER STORES
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormEtudiant {
    
    private Long id;
    private LocalDate dateDeNaissance;
    private String email;
    private Integer genre;
    private String lieuDeNaissance;
    private String matricule;
    private String nom;
    private String region;
    private Boolean validDate;
    private String numeroTelephone;
}
