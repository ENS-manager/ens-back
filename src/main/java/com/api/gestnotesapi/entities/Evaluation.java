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
public class Evaluation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CODE")
    private String code;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ISEXAM")
    private Boolean isExam;

    @Column(name = "POURCENTAGE")
    private Integer pourcentage;

    @JoinColumn(name = "TYPECOURS_ID")
    @ManyToOne
    private TypeCours typeCours;

//    @OneToMany(mappedBy = "evaluation")
//    private List<EvaluationDetails> eds = new ArrayList<>();
    
    @OneToMany(mappedBy = "evaluation")
    @JsonIgnore
    private List<Note> notes = new ArrayList<>();
}