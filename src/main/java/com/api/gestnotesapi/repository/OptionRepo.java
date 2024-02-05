package com.api.gestnotesapi.repository;

import com.api.gestnotesapi.entities.Departement;
import com.api.gestnotesapi.entities.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OptionRepo extends JpaRepository<Option, Long> {
    public Optional<Option> findByCode(String code);

    List<Option> findAllByDepartement(Departement departement);
}
