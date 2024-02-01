/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.api.gestnotesapi.repository;

//import com.api.gestnotesapi.dto.FormAnonymat;
import com.api.gestnotesapi.entities.AnneeAcademique;
import com.api.gestnotesapi.entities.Anonymat;
import com.api.gestnotesapi.entities.Cours;
import com.api.gestnotesapi.entities.Etudiant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


/**
 *   
 * @author COMPUTER STORES
 */

public interface AnonymatRepo extends JpaRepository<Anonymat, Long> {

    Anonymat findByValeur(String valeur);


    List<Anonymat> findAllByAnneeAcademiqueAndCoursAndSessions(AnneeAcademique anneeAcademique, Cours cours, int session);

}
