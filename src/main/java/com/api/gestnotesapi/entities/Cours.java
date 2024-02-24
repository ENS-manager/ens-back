
package com.api.gestnotesapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.api.gestnotesapi.entities.Departement;

import java.util.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Cours implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long coursId;

//    auto
    @Column(name = "CODE", unique = true)
    private String code;

    @Column(name = "NATUREUE")
    private NatureUE natureUE;
    
    @Column(name = "INTITULE")
    private String intitule;

    @Column(name = "ACTIVE")
    private Boolean active = true;

    @Column(name = "ISSTAGE")
    private Boolean isStage = false;

//    @ManyToOne
//    @JoinColumn(name = "DEPARTEMENT_ID")
//    private Departement departement;

    @ManyToOne
    @JoinColumn(name = "CREDIT_ID")
    private Credit credit;
    
    @ManyToOne
    @JoinColumn(name = "TYPECOURS_ID")
    private TypeCours typecours;

//    @ManyToOne
//    @JoinColumn(name = "PARCOURS_ID")
//    private Parcours parcours;

    @ManyToOne
    @JoinColumn(name = "SEMESTRE_ID")
    private Semestre semestre;

    @OneToMany(mappedBy = "cours", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Note> notes = new ArrayList<>();

    @OneToMany(mappedBy = "cours", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Programme> prorammes = new ArrayList<>();

    @OneToMany(mappedBy = "cours", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Module> modules = new ArrayList<>();

    @OneToMany(mappedBy = "cours", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Moyenne> moyennes = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "COURS_ENSEIGNANT",
            joinColumns = @JoinColumn(name = "cours_ID"),
            inverseJoinColumns = @JoinColumn(name = "enseignants_ID")
    )
    @JsonIgnore
    private List<Enseignant> enseignant = new ArrayList<>();

    @ManyToMany(mappedBy = "cours", cascade = CascadeType.ALL)
    private List<Parcours> parcours = new ArrayList<>();
}
