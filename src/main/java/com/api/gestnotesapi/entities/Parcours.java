/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.gestnotesapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.io.Serializable;
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
@Table(name = "PARCOURS", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"NIVEAU_ID", "OPTIONS_ID"}, name = "UNQ_PARCOURS_0")
})
public class Parcours implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

//    auto
    @Column(name = "LABEL")
    private String label;

    @Column(name = "ACTIVE")
    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "NIVEAU_ID")
    private Niveau niveau;

    @ManyToOne
    @JoinColumn(name = "OPTIONS_ID")
    private Option option;
    
//    @OneToMany(mappedBy = "parcours")
//    @JsonIgnore
//    private List<Credit> credits = new ArrayList<>();
    
//    @OneToMany(mappedBy = "parcours")
//    private List<Enseignement> enseignements = new ArrayList<>();
//
//    @OneToMany(mappedBy = "parcours")
//    @JsonIgnore
//    private List<Cours> cours = new ArrayList<>();
//
    @OneToMany(mappedBy = "parcours", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Programme> programmes = new ArrayList<>();
    
    @OneToMany(mappedBy = "parcours", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Inscription> inscriptions = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "COURS_PARCOURS",
            joinColumns = @JoinColumn(name = "PARCOURS_ID"),
            inverseJoinColumns = @JoinColumn(name = "COURS_ID")
    )
    @JsonIgnore
    private List<Cours> cours = new ArrayList<>();

}
