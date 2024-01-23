
package com.api.gestnotesapi.repository;

import com.api.gestnotesapi.entities.Niveau;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NiveauRepo extends JpaRepository<Niveau, Long> {

    Optional<Niveau> findByValeur(int valeur);
}
