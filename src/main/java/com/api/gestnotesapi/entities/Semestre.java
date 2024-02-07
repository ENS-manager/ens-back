package com.api.gestnotesapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Semestre implements Serializable {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "VALEUR")
    private Integer valeur;

    @Column(name = "ACTIVE")
    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "NIVEAU_ID")
    private Niveau niveau;
    
    @OneToMany(mappedBy = "semestre")
    @JsonIgnore
    private List<Programme> programmes = new ArrayList<>();

    @OneToMany(mappedBy = "semestre")
    @JsonIgnore
    private List<Cours> cours = new ArrayList<>();

//    @OneToMany(mappedBy = "semestre")
//    @JsonIgnore
//    private List<Module> modules = new ArrayList<>();

}
