package com.api.gestnotesapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
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

//    auto
    @Column(name = "NUMERODEBUT", unique = true)
    private Integer numeroDebut;

//    auto
    @Column(name = "CODE", unique = true)
    private String code;

    @OneToMany(mappedBy = "anneeAcademique")
    @JsonIgnore
    private List<Anonymat> anonymats = new ArrayList<>();
    
    @OneToMany(mappedBy = "anneeAcademique")
    @JsonIgnore
    private List<Programme> programmes = new ArrayList<>();
    
    @OneToMany(mappedBy = "anneeAcademique")
    @JsonIgnore
    private List<Note> notes = new ArrayList<>();
    
    @OneToMany(mappedBy = "anneeAcademique")
    @JsonIgnore
    private List<Inscription> inscriptions = new ArrayList<>();
    
//    @OneToMany(mappedBy = "anneeAcademique")
//    @JsonIgnore
//    private List<Credit> credits = new ArrayList<>();

}
