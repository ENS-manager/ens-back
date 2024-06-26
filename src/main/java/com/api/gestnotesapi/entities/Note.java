/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.gestnotesapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author COMPUTER STORES
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "NOTE", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ETUDIANT_ID", "ANNEEACADEMIQUE_ID", "COURS_ID", "MODULE_ID", "EVALUATION_ID", "ISFINAL", "SESSIONS", "ACTIVE"}, name = "UNQ_NOTE_0"),
        @UniqueConstraint(columnNames = {"ETUDIANT_ID", "ANNEEACADEMIQUE_ID", "COURS_ID", "EVALUATION_ID", "ISFINAL", "SESSIONS", "ACTIVE"}, name = "UNQ_NOTE_1"),
})
public class Note implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "SESSIONS")
    private Integer sessions = 0;

    @Column(name = "VALEUR")
    private Double valeur;

    @Column(name = "ISFINAL")
    private Boolean isFinal = false;

    @Column(name = "ACTIVE")
    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "ANNEEACADEMIQUE_ID")
    private AnneeAcademique anneeAcademique;

    @ManyToOne
    @JoinColumn(name = "COURS_ID")
    private Cours cours;

    @ManyToOne
    @JoinColumn(name = "MODULE_ID")
    private Module module;

    @ManyToOne
    @JoinColumn(name = "ETUDIANT_ID")
    private Etudiant etudiant;

    @ManyToOne
    @JoinColumn(name = "EVALUATION_ID")
    private Evaluation evaluation;
}
