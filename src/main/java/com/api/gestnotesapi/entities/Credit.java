/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.gestnotesapi.entities;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;

/**
 *
 * @author COMPUTER STORES
 */
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CREDIT"
//        , uniqueConstraints = {
//    @UniqueConstraint(columnNames = {"ANNEEACADEMIQUE_ID", "PARCOURS_ID"}, name = "UNQ_CREDIT_0")
//}
)
public class Credit implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "VALEUR")
    private Integer valeur;

//    @ManyToOne
//    @JoinColumn(name = "ANNEEACADEMIQUE_ID")
//    private AnneeAcademique anneeAcademique;
//
//    @ManyToOne
//    @JoinColumn(name = "PARCOURS_ID")
//    private Parcours parcours;

    @OneToMany(mappedBy = "credit")
    @JsonIgnore
    private List<Cours> cours = new ArrayList<>();
}
