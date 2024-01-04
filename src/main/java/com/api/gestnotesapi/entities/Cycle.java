package com.api.gestnotesapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
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
public class Cycle implements Serializable {

   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "DIPLOMEEN", unique = true)
    private String diplomeEn;

    @Column(name = "DIPLOMEFR", unique = true)
    private String diplomeFr;

    @Column(name = "ESTAFFICHABLE")
    private Boolean estAffichable;

    @Column(name = "CODE", unique = true)
    private String code;

    @OneToMany(mappedBy = "cycle")
    @JsonIgnore
    private List<Niveau> niveaux = new ArrayList<>();

}
