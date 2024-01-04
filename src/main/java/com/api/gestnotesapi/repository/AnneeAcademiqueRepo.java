
package com.api.gestnotesapi.repository;

import com.api.gestnotesapi.entities.AnneeAcademique;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AnneeAcademiqueRepo extends JpaRepository<AnneeAcademique, Long> {

    public AnneeAcademique findByNumeroDebut(int numeroDebut);
}

