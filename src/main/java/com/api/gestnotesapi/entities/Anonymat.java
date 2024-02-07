/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.gestnotesapi.entities;
import jakarta.persistence.*;

import java.io.Serializable;
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

//UNIQUE KEY `UNQ_ANONYMAT_0` (`VALEUR`,`COURS_ID`,`NIVEAU_ID`,`ANNEEACADEMIQUE_ID`,`SESSIONS`),
//UNIQUE KEY `ETUDIANT_ID` (`ETUDIANT_ID`,`COURS_ID`,`ANNEEACADEMIQUE_ID`,`SESSIONS`),
@Table(name = "ANONYMAT"
        , uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ANNEEACADEMIQUE_ID", "ETUDIANT_ID","COURS_ID","SESSIONS"}, name = "ETUDIANT_ID"),
    @UniqueConstraint(columnNames = {"VALEUR","COURS_ID","ANNEEACADEMIQUE_ID","SESSIONS"}, name = "UNQ_ANONYMAT_0")
})
public class Anonymat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    
    @Column(name = "SESSIONS")
    private Integer sessions = 1;

//    auto
    @Column(name = "VALEUR", unique = true)
    private String valeur;

    @Column(name = "ACTIVE")
    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "ANNEEACADEMIQUE_ID")
    private AnneeAcademique anneeAcademique;
    
    @ManyToOne
    @JoinColumn(name = "COURS_ID")
    private Cours cours;
    
    @ManyToOne
    @JoinColumn(name = "ETUDIANT_ID")
    private Etudiant etudiant;

}
