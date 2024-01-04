package com.api.gestnotesapi.repository;

import com.api.gestnotesapi.entities.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModuleRepo extends JpaRepository<Module, Long> {
    public Optional<Module> findByCode(String code);
}
