/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.gestnotesapi.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author COMPUTER STORES
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Etudiant implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "DATEDENAISSANCE")
    private LocalDate dateDeNaissance;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "GENRE")
    private Integer genre;

    @Column(name = "LIEUDENAISSANCE")
    private String lieuDeNaissance;

    @Column(name = "MATRICULE", unique = true)
    private String matricule;

    @Column(name = "NOM")
    private String nom;
    
    @Column(name = "REGION")
    private String region;

    @Column(name = "NUMEROTELEPHONE")
    private String numeroTelephone;

    @Column(name = "VALIDE")
    private Boolean valide = false;
    
    @OneToMany(mappedBy = "etudiant")
    @JsonIgnore
    private List<Anonymat> anonymats = new ArrayList<>();
    
    @OneToMany(mappedBy = "etudiant")
    @JsonIgnore
    private List<Inscription> inscriptions = new ArrayList<>();
    
    @OneToMany(mappedBy = "etudiant")
    @JsonIgnore
    private List<Note> notes = new ArrayList<>();

    
}
