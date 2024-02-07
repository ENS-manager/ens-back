
package com.api.gestnotesapi.entities;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Enseignant implements Serializable {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOM")
    private String nom;

    @Column(name = "ACTIVE")
    private Boolean active = true;

//    cours dispense

    @ManyToMany(mappedBy = "enseignant")
//    @JoinTable(
//        name = "ENSEIGNANT_COURS",
//        joinColumns = @JoinColumn(name = "enseignants_ID"),
//        inverseJoinColumns = @JoinColumn(name = "cours_ID")
//    )
    private List<Cours> cours = new ArrayList<>();
}
