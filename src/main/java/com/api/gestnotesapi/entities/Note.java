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
    @UniqueConstraint(columnNames = {"ETUDIANT_ID", "ANNEEACADEMIQUE_ID", "COURS_ID", "MODULE_ID", "EVALUATION_ID", "ISFINAL", "SESSIONS"}, name = "UNQ_NOTE_0")})
public class Note implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "SESSIONS")
    private Integer sessions = 1;

    @Column(name = "VALEUR")
    private Double valeur;

    @Column(name = "ISFINAL")
    private Boolean isFinal = false;

    @ManyToOne
    @JoinColumn(name = "ANNEEACADEMIQUE_ID")
    @JsonIgnore
    private AnneeAcademique anneeAcademique;

    @ManyToOne
    @JoinColumn(name = "COURS_ID")
    @JsonIgnore
    private Cours cours;

    @ManyToOne
    @JoinColumn(name = "MODULE_ID")
    @JsonIgnore
    private Module module;

    @ManyToOne
    @JoinColumn(name = "ETUDIANT_ID")
    @JsonIgnore
    private Etudiant etudiant;

    @ManyToOne
    @JoinColumn(name = "EVALUATION_ID")
    private Evaluation evaluation;
}
