package com.api.gestnotesapi.repository;

import com.api.gestnotesapi.entities.Credit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditRepo extends JpaRepository<Credit, Long> {
}
