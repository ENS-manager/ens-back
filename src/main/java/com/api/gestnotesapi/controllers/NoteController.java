package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.dto.PVCoursResponse;
import com.api.gestnotesapi.dto.PVModuleRequest;
import com.api.gestnotesapi.dto.PVModuleResponse;
import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.services.NoteService;
import com.api.gestnotesapi.services.PVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin("*")
//@RequestMapping("/api/v1/admin/note")
public class NoteController {

    private NoteService noteService;
    private PVService pvService;

    @Autowired
    public NoteController(NoteService noteService, PVService pvService) {
        this.noteService = noteService;
        this.pvService = pvService;
    }

    //  Ajouter une Note
    @PostMapping("/addNoteCours")
    public ResponseEntity<Note> saveNoteCours(@RequestBody Note note){

        Note noteSave = noteService.ajouterNoteCours(note);
        if (noteSave == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(noteSave, HttpStatus.OK);
    }

    @PostMapping("/addNoteModule")
    public ResponseEntity<Note> saveNoteModule(@RequestBody Note note){

        Note noteSave = noteService.ajouterNoteModule(note);
        if (noteSave == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(noteSave, HttpStatus.OK);
    }

//    Ajouter une note d'examen avec comme entrees: le numero d'anonymat et la note
    @PostMapping("/addNoteExamen/anonymat/{valeur}/note")
    public ResponseEntity<Note> saveNoteExamen(@PathVariable String valeur, @RequestParam(name = "note", required = false) Double noteUE){

//        if (noteUE == null){
//            noteUE = -1.0;
//        }
        Note noteSave = noteService.ajouterNoteExamen(valeur, noteUE);
        if (noteSave == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(noteSave, HttpStatus.OK);
    }

    //    Moyenne ponderee des notes de cc, tpe et tp d'une session, pour une annee academique et un cours
    @GetMapping("/findMoyenneNotesModuleByAnneeAca/{year}/codeUE")
    public ResponseEntity<String> getNoteByCode(@PathVariable int year, @RequestParam("code") String code){

        String result = noteService.caculMoyennePondere(year, code);
        if (result == null){
            return new ResponseEntity<>("Aucune note a calculer", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/find/{id}/{annee}/cours")
    public Double getMoy(@PathVariable Long id, @PathVariable int annee, @RequestParam String code){
        return noteService.calculMoyenneCours(id, code, annee);
    }

    //    Proces verbal d'un cours
    @GetMapping("/findPVCours/session/{session}/annee/{annee}/cours")
    public ResponseEntity<List<PVCoursResponse>> getPVCours(@PathVariable int session, @PathVariable int annee,
                                                            @RequestParam String code, @RequestParam String label){

        List<PVCoursResponse> response = pvService.getPVCoursByEtudiant(session, annee, code, label);
        if (response == null){
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //    Proces verbal d'un module
    @GetMapping("/findPVModule")
    public ResponseEntity<PVModuleResponse> getPVModule(@RequestBody PVModuleRequest pvModuleRequest){

        PVModuleResponse response = pvService.getPVModuleByEtudiant(pvModuleRequest);
        if (response == null){
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    revoir
    //    Liste des passages de niveau par parcours et type d'etudiant
    @GetMapping("/findListPassageByParcours/{type}/anneeAca/{year}/parcours")
    public ResponseEntity<List<Etudiant>> listPassageByParcours(@RequestParam String label, @PathVariable int year){

        List<Etudiant> etudiantList = noteService.getListPassageByParcours(label, year);
        if (etudiantList.isEmpty()){
            new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(etudiantList, HttpStatus.OK);
    }

    //    Modifier une Note
    @PutMapping("/updateNote/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable("id") Long id, @RequestBody Note note){

        Note noteFromDb = noteService.update(id, note);
        if (noteFromDb == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(noteFromDb, HttpStatus.OK);
    }

    //    Supprimer une Note
    @PutMapping("/deleteNote/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable("id") Long id){
        return new ResponseEntity<>(noteService.delete(id), HttpStatus.OK);
    }
}