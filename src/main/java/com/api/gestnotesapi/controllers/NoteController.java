package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.dto.PVCoursResponse;
import com.api.gestnotesapi.dto.PVCoursSansEEResponse;
import com.api.gestnotesapi.dto.PVModuleResponse;
import com.api.gestnotesapi.dto.StatPassedDepart;
import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.services.NoteService;
import com.api.gestnotesapi.services.PVService;
import com.api.gestnotesapi.services.Statistiques;
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
    private Statistiques statistiques;

    @Autowired
    public NoteController(NoteService noteService, PVService pvService, Statistiques statistiques) {
        this.noteService = noteService;
        this.pvService = pvService;
        this.statistiques = statistiques;
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

    //    Proces verbal d'un cours sans Examen
    @GetMapping("/findPVCours/annee/{annee}/cours")
    public ResponseEntity<List<PVCoursSansEEResponse>> getPVCoursSansEE(@PathVariable int annee, @RequestParam String code,
                                                                        @RequestParam String label){

        List<PVCoursSansEEResponse> response = pvService.getPVCoursSansEEResponse(annee, code, label);
        if (response == null){
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //    Proces verbal d'un module
    @GetMapping("/findPVModule/annee/{annee}/module")
    public ResponseEntity<List<PVModuleResponse>> getPVModule(@PathVariable int annee,
                                                              @RequestParam String code, @RequestParam String label){
        List<PVModuleResponse> response = pvService.getPVModuleByEtudiant(annee, code, label);
        if (response == null){
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //    Liste des passages de niveau par parcours et type d'etudiant
    @GetMapping("/findListPassageByParcours/{type}/anneeAca/{year}/parcours")
    public ResponseEntity<List<Etudiant>> listPassageByParcours(@RequestParam String label, @PathVariable int year){

        List<Etudiant> etudiantList = noteService.getListPassageByParcours(label, year);
        if (etudiantList.isEmpty()){
            new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(etudiantList, HttpStatus.OK);
    }

//    Le nombre d'annee academique actif
    @GetMapping("/findAllActifAnneeAca")
    public ResponseEntity<Integer> getAllActifAnneeAca(){
        return new ResponseEntity<>(statistiques.statAnneeActif(), HttpStatus.OK);
    }

//    Le nombre de departement actif
    @GetMapping("/findAllActifDepartement")
    public ResponseEntity<Integer> getAllActifDepartement(){
        return new ResponseEntity<>(statistiques.statDepartActif(), HttpStatus.OK);
    }

    //    Le nombre d'etudiant actif
    @GetMapping("/findAllActifEtudiant")
    public ResponseEntity<Integer> getAllActifEtudiant(){
        return new ResponseEntity<>(statistiques.statAllEtudiantActif(), HttpStatus.OK);
    }

    //    Le nombre d'etudiant actif par departement
    @GetMapping("/findAllActifEtudiantDepartement")
    public ResponseEntity<Integer> getAllActifEtudiantDepartement(@RequestParam String code){
        return new ResponseEntity<>(statistiques.statEtudiantDepartementActif(code), HttpStatus.OK);
    }

    //    Le nombre d'etudiant actif par parcours
    @GetMapping("/findAllActifEtudiantParcours")
    public ResponseEntity<Integer> getAllActifEtudiantParcours(@RequestParam String label){
        return new ResponseEntity<>(statistiques.statEtudiantParcoursActif(label), HttpStatus.OK);
    }

    //    Le nombre de cours actif
    @GetMapping("/findAllActifCours")
    public ResponseEntity<Integer> getAllActifCours(){
        return new ResponseEntity<>(statistiques.statAllCoursActif(), HttpStatus.OK);
    }

    //    Le nombre de cours actif par departement
    @GetMapping("/findAllActifCoursDepartement")
    public ResponseEntity<Integer> getAllActifCoursDepartement(@RequestParam String code){
        return new ResponseEntity<>(statistiques.statAllCoursDepartementActif(code), HttpStatus.OK);
    }

    //    Le nombre de parcours actif
    @GetMapping("/findAllActifParcours")
    public ResponseEntity<Integer> getAllActifParcours(){
        return new ResponseEntity<>(statistiques.statAllParcoursActif(), HttpStatus.OK);
    }

    //    Le nombre de parcours actif par departement
    @GetMapping("/findAllActifParcoursDepartement")
    public ResponseEntity<Integer> getAllActifParcoursDepartement(@RequestParam String code){
        return new ResponseEntity<>(statistiques.statAllParcoursDepartementActif(code), HttpStatus.OK);
    }

    //    Le taux de reussite de l'ecole pour chaque departement et par annee
    @GetMapping("/findPassedStats")
    public ResponseEntity<List<StatPassedDepart>> getPassedStats(){
        List<StatPassedDepart> statPassedDepartList = statistiques.statPassedAll();
        if (statPassedDepartList == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(statPassedDepartList, HttpStatus.OK);
    }

    //    Le taux de reussite par departement
    @GetMapping("/findPassedStatsDepartement/annee/{year}/departement")
    public ResponseEntity<Double> getPassedStatsDepartement(@PathVariable int year, @RequestParam String code){
        return new ResponseEntity<>(statistiques.statPassedByDepartement(code, year), HttpStatus.OK);
    }

    //    Le taux de reussite par parcours
    @GetMapping("/findPassedStatsParcours/annee/{year}/parcours")
    public ResponseEntity<Double> getPassedStatsParcours(@PathVariable int year, @RequestParam String label){
        return new ResponseEntity<>(statistiques.statPassedByParcours(label, year), HttpStatus.OK);
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