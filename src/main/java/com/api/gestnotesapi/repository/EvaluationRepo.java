package com.api.gestnotesapi.repository;

import com.api.gestnotesapi.entities.CodeEva;
import com.api.gestnotesapi.entities.Evaluation;
import com.api.gestnotesapi.entities.TYPE;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EvaluationRepo extends JpaRepository<Evaluation, Long> {
     Optional<Evaluation> findByCode(CodeEva code);
}
