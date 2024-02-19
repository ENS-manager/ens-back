
package com.api.gestnotesapi.repository;

import com.api.gestnotesapi.entities.AnneeAcademique;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AnneeAcademiqueRepo extends JpaRepository<AnneeAcademique, Long> {

    AnneeAcademique findByNumeroDebut(int numeroDebut);
    Optional<AnneeAcademique> findByCode(String code);
}

