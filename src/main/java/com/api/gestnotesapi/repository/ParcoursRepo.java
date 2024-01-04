package com.api.gestnotesapi.repository;

import com.api.gestnotesapi.entities.Parcours;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParcoursRepo extends JpaRepository<Parcours, Long> {
    public Optional<Parcours> findByLabel(String label);
}
