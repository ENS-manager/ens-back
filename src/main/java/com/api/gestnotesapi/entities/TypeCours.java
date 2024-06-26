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
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TYPECOURS")
public class TypeCours implements Serializable {
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOM", unique = true)
    private TYPECOURSENUM nom;

    @Column(name = "ACTIVE")
    private Boolean active = true;
    
    @OneToMany(mappedBy = "typecours", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Cours> cours = new ArrayList<>();
//
//    @OneToMany(mappedBy = "typecours")
//    private List<EvaluationDetails> eds = new ArrayList<>();
}
