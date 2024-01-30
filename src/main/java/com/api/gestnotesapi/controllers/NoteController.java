package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.dto.PVCoursRequest;
import com.api.gestnotesapi.dto.PVCoursResponse;
import com.api.gestnotesapi.dto.PVModuleRequest;
import com.api.gestnotesapi.dto.PVModuleResponse;
import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.repository.*;
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

    private NoteRepo noteRepo;
    private CoursRepo coursRepo;
    private AnneeAcademiqueRepo anneeAcademiqueRepo;
    private EvaluationRepo evaluationRepo;
    private ModuleRepo moduleRepo;
    private CreditRepo creditRepo;
    private EtudiantRepo etudiantRepo;
    private ParcoursRepo parcoursRepo;
    private InscriptionRepo inscriptionRepo;
    private AnonymatRepo anonymatRepo;
    private TypeCoursRepo typeCoursRepo;
    private SemestreRepo semestreRepo;
    private NiveauRepo niveauRepo;
    private CycleRepo cycleRepo;
    private NoteService noteService;
    private PVService pvService;

    @Autowired
    public NoteController(NoteRepo noteRepo, CoursRepo coursRepo, AnneeAcademiqueRepo anneeAcademiqueRepo, EvaluationRepo evaluationRepo, ModuleRepo moduleRepo, CreditRepo creditRepo, EtudiantRepo etudiantRepo, ParcoursRepo parcoursRepo, InscriptionRepo inscriptionRepo, AnonymatRepo anonymatRepo, TypeCoursRepo typeCoursRepo, SemestreRepo semestreRepo, NiveauRepo niveauRepo, CycleRepo cycleRepo, NoteService noteService, PVService pvService) {
        this.noteRepo = noteRepo;
        this.coursRepo = coursRepo;
        this.anneeAcademiqueRepo = anneeAcademiqueRepo;
        this.evaluationRepo = evaluationRepo;
        this.moduleRepo = moduleRepo;
        this.creditRepo = creditRepo;
        this.etudiantRepo = etudiantRepo;
        this.parcoursRepo = parcoursRepo;
        this.inscriptionRepo = inscriptionRepo;
        this.anonymatRepo = anonymatRepo;
        this.typeCoursRepo = typeCoursRepo;
        this.semestreRepo = semestreRepo;
        this.niveauRepo = niveauRepo;
        this.cycleRepo = cycleRepo;
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

        if (noteUE == null){
            noteUE = -1.0;
        }
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

//    Proces verbal d'un cours
    @GetMapping("/findPVCours")
    public ResponseEntity<List<PVCoursResponse>> getPVCours(@RequestBody PVCoursRequest pvCoursRequest){

        List<PVCoursResponse> response = pvService.getPVCoursByEtudiant(pvCoursRequest);
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
    public ResponseEntity<List<Etudiant>> listPassageByParcours(@RequestParam String label, @PathVariable int year, @PathVariable("type") TYPE type){

        List<Etudiant> etudiantList = noteService.getListPassageByParcours(label, year, type);
        if (etudiantList.isEmpty()){
            new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(etudiantList, HttpStatus.OK);
    }

    //    Modifier une Note
    @PutMapping("/updateNote/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable("id") Long id, @RequestBody Note note){

        Note noteFromDb = noteRepo.findById(id).orElse(null);
        if (noteFromDb == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        noteFromDb.setValeur(note.getValeur());
        noteFromDb.setModule(note.getModule());
        noteFromDb.setCours(note.getCours());
        noteFromDb.setAnneeAcademique(note.getAnneeAcademique());
        noteFromDb.setEtudiant(note.getEtudiant());
        noteFromDb.setEvaluation(note.getEvaluation());

        return new ResponseEntity<>(noteRepo.save(noteFromDb), HttpStatus.OK);
    }

    //    Supprimer une Note
    @DeleteMapping("/deleteNote/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable("id") Long id){
        noteRepo.deleteById(id);
        return new ResponseEntity<>("Deleted with Successfully from database", HttpStatus.OK);
    }
}