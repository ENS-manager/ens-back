package com.api.gestnotesapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import jakarta.persistence.OneToMany;
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
public class Niveau implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "VALEUR", unique = true)
    private Integer valeur;

    @Column(name = "TERMINAL")
    private Boolean terminal;

    @ManyToOne
    @JoinColumn(name = "CYCLE_ID")
    @JsonIgnore
    private Cycle cycle;
    
    @OneToMany(mappedBy = "niveau")
    @JsonIgnore
    private List<Semestre> semestres = new ArrayList<>();

    @OneToMany(mappedBy = "niveau")
    @JsonIgnore
    private List<Parcours> parcours = new ArrayList<>();

}
