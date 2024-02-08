
package com.api.gestnotesapi.repository;

import com.api.gestnotesapi.entities.Departement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.security.PublicKey;
import java.util.Optional;


public interface DepartementRepo extends JpaRepository<Departement, Long>{

    Optional<Departement> findByCode(String code);

    Departement findByCodeAndActive(String code, boolean b);
}
