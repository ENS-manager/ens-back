
package com.api.gestnotesapi.repository;

import com.api.gestnotesapi.entities.AnneeAcademique;
import com.api.gestnotesapi.entities.Cours;
import java.util.List;
import java.util.Optional;

import com.api.gestnotesapi.entities.Departement;
import com.api.gestnotesapi.entities.Semestre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CoursRepo extends JpaRepository<Cours, Long>{
    
    
   
//    @Query(value = """
//                   SELECT c.*
//                   FROM uniteenseignement_cours u,cours c
//                   WHERE c.ID = u.cours_ID and uniteEnseignements_ID=:ueId""",nativeQuery = true)
//    List<Cours> ListCoursUe(@Param("ueId") Long ueId);
        
    Optional<Cours> findByCode(String code);

    Cours findByCoursId(Long id);

    List<Cours> findBySemestreAndDepartement(Semestre semestre, Departement departement);
}
