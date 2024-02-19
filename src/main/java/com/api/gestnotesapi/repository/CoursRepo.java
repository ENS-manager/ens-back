
package com.api.gestnotesapi.repository;

import com.api.gestnotesapi.entities.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CoursRepo extends JpaRepository<Cours, Long>{
        
    Optional<Cours> findByCode(String code);

    Cours findByCoursId(Long id);

}
