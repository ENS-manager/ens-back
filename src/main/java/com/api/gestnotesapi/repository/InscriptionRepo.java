package com.api.gestnotesapi.repository;

import com.api.gestnotesapi.entities.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscriptionRepo extends JpaRepository<Inscription, Long> {
}
