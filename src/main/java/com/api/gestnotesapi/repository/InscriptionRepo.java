package com.api.gestnotesapi.repository;

import com.api.gestnotesapi.entities.AnneeAcademique;
import com.api.gestnotesapi.entities.Etudiant;
import com.api.gestnotesapi.entities.Inscription;
import com.api.gestnotesapi.entities.Parcours;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InscriptionRepo extends JpaRepository<Inscription, Long> {

    List<Inscription> findAllByParcoursAndAnneeAcademique(Parcours parcours, AnneeAcademique anneeAcademique);

    Inscription findByEtudiantAndAnneeAcademique(Etudiant etudiant, AnneeAcademique anneeAcademique);
}
