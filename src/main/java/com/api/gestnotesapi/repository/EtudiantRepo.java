/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.api.gestnotesapi.repository;


import com.api.gestnotesapi.entities.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 *
 * @author COMPUTER STORES
 */
public interface EtudiantRepo extends JpaRepository<Etudiant, Long> {
    
    Optional<Etudiant> findByMatricule(String matricule);
}
