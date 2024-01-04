package com.api.gestnotesapi.repository;

import com.api.gestnotesapi.entities.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EvaluationRepo extends JpaRepository<Evaluation, Long> {
    public Optional<Evaluation> findByCode(String code);
}
