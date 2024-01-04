/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.api.gestnotesapi.repository;


import com.api.gestnotesapi.entities.Note;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author COMPUTER STORES
 */
public interface NoteRepo extends JpaRepository<Note, Long> {
    
     @Query(value = "SELECT * FROM NOTE WHERE COURS_ID = :coursId", nativeQuery = true)
     List<Note> ListNoteEc(@Param("coursId") Long coursId);
    
}
