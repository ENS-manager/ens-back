package com.api.gestnotesapi.repository;

import com.api.gestnotesapi.entities.AnneeAcademique;
import com.api.gestnotesapi.entities.Cours;
import com.api.gestnotesapi.entities.Etudiant;
import com.api.gestnotesapi.entities.Moyenne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoyenneRepo extends JpaRepository<Moyenne, Long> {
    List<Moyenne> findAllByEtudiantAndCours(Etudiant etudiant, Cours cours);

    List<Moyenne> findAllByEtudiantAndCoursAndAnneeAcademique(Etudiant etudiant, Cours cours, AnneeAcademique anneeAcademique);

    Moyenne findAllByEtudiantAndCoursAndAnneeAcademiqueAndSessionAndValeur(Etudiant etudiant, Cours cours, AnneeAcademique anneeAcademique, int session, Double valeur);

}
