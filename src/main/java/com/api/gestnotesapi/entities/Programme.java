/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.gestnotesapi.entities;

/**
 *
 * @author COMPUTER STORES
 */
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PROGRAMME", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"SEMESTRE_ID", "ANNEEACADEMIQUE_ID", "PARCOURS_ID", "COURS_ID"}, name = "UNQ_PROGRAMME_0")
})
public class Programme implements Serializable {
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @JsonIgnore
    @XmlTransient
    @ManyToOne
    @JoinColumn(name = "ANNEEACADEMIQUE_ID")
    private AnneeAcademique anneeAcademique;

    @JsonIgnore
    @XmlTransient
    @ManyToOne
    @JoinColumn(name = "PARCOURS_ID")
    private Parcours parcours;

    @JsonIgnore
    @XmlTransient
    @ManyToOne
    @JoinColumn(name = "SEMESTRE_ID")
    private Semestre semestre;

    @JsonIgnore
    @XmlTransient
    @ManyToOne
    @JoinColumn(name = "COURS_ID")
    private Cours cours;

}
