/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.api.gestnotesapi.repository;


import com.api.gestnotesapi.entities.*;

import java.util.List;
import java.util.Optional;

import com.api.gestnotesapi.entities.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author COMPUTER STORES
 */
public interface NoteRepo extends JpaRepository<Note, Long> {
    
     List<Note> findAllByEtudiantAndAnneeAcademiqueAndCoursAndIsFinal(Etudiant etudiant, AnneeAcademique anneeAcademique, Cours cours, boolean b);

    List<Note> findAllByCoursAndAnneeAcademiqueAndIsFinal(Cours cours, AnneeAcademique anneeAcademique, boolean b);

    List<Note> findAllByCoursAndIsFinalAndAnneeAcademique(Cours cours, boolean b, AnneeAcademique anneeAcademique);

    Note[] findByCoursAndEtudiantAndAnneeAcademiqueAndIsFinal(Cours cours, Etudiant etudiant, AnneeAcademique anneeAcademique, boolean b);

    List<Note> findAllByModuleAndAnneeAcademiqueAndIsFinal(Module module, AnneeAcademique anneeAcademique, boolean b);

    List<Note> findAllByEtudiantAndAnneeAcademiqueAndModule(Etudiant etudiant, AnneeAcademique anneeAcademique, Module module);
}
