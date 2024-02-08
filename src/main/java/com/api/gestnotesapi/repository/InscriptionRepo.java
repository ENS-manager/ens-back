package com.api.gestnotesapi.repository;

import com.api.gestnotesapi.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InscriptionRepo extends JpaRepository<Inscription, Long> {

    Inscription findByEtudiantAndAnneeAcademique(Etudiant etudiant, AnneeAcademique anneeAcademique);

    List<Inscription> findAllByAnneeAcademique(AnneeAcademique anneeAcademique);

    List<Inscription> findAllByParcoursAndAnneeAcademiqueAndActive(Parcours parcours, AnneeAcademique anneeAcademique, boolean b);
}
