package com.api.gestnotesapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "MOYENNE", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ETUDIANT_ID", "ANNEEACADEMIQUE_ID", "COURS_ID", "SESSION", "VALEUR"}, name = "UNQ_NOTE_0")
})
public class Moyenne implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double valeur;
    private Integer session;

    @ManyToOne
    @JoinColumn(name = "ETUDIANT_ID")
    private Etudiant etudiant;
    @ManyToOne
    @JoinColumn(name = "COURS_ID")
    private Cours cours;
    @ManyToOne
    @JoinColumn(name = "ANNEEACADEMIQUE_ID")
    private AnneeAcademique anneeAcademique;
}
