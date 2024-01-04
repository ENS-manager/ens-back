
package com.api.gestnotesapi.repository;

import com.api.gestnotesapi.entities.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EnseignantRepo extends JpaRepository<Enseignant, Long> {
    
}
