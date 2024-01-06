package com.api.gestnotesapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Module implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CODE", unique = true)
    private String code;

//    @Column(name = "NATUREUE")
//    private NatureUE natureUE;

    @Column(name = "INTITULE")
    private String intitule;

    @ManyToOne
    @JoinColumn(name = "COURS_ID")
    private Cours cours;

//    @ManyToOne
//    @JoinColumn(name = "SEMESTRE_ID")
//    private Semestre semestre;

    @ManyToOne
    @JoinColumn(name = "CREDIT_ID")
    private Credit credit;
}
