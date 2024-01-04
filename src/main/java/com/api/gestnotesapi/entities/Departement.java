package com.api.gestnotesapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Departement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CODE", unique = true)
    private String code;

    @Column(name = "ENGLISHDESCRIPTION")
    private String englishDescription;

    @Column(name = "FRENCHDESCRIPTION")
    private String frenchDescription;
    
    @OneToMany(mappedBy = "departement")
    @JsonIgnore
    private List<Cours> cours = new ArrayList<>();
    
    @OneToMany(mappedBy = "departement")
    @JsonIgnore
    private List<Option> options = new ArrayList<>();
}
