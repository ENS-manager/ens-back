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

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ANNEEACADEMIQUE")
public class AnneeAcademique implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    
    @Column(name = "DEBUT")
    private LocalDate debut;
    
    @Column(name = "FIN")
    private LocalDate fin;

    @Column(name = "ACTIVE")
    private Boolean active = true;

//    auto
    @Column(name = "NUMERODEBUT", unique = true)
    private Integer numeroDebut;

//    auto
    @Column(name = "CODE", unique = true)
    private String code;

    @OneToMany(mappedBy = "anneeAcademique", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Moyenne> moyennes = new ArrayList<>();
    
    @OneToMany(mappedBy = "anneeAcademique", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Programme> programmes = new ArrayList<>();
    
    @OneToMany(mappedBy = "anneeAcademique", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Note> notes = new ArrayList<>();
    
    @OneToMany(mappedBy = "anneeAcademique", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Inscription> inscriptions = new ArrayList<>();

}
