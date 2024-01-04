/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.api.gestnotesapi.repository;

import com.api.gestnotesapi.entities.Programme;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author COMPUTER STORES
 */
public interface ProgrammeRepo extends JpaRepository<Programme, Long> {
    
}
