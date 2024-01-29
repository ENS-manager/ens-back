package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.entities.Module;
import com.api.gestnotesapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
//@RequestMapping("/api/v1/admin/evaluation")
public class EvaluationController {

    @Autowired
    private EvaluationRepo evaluationRepo;
    @Autowired
    private CoursRepo coursRepo;
    @Autowired
    private AnneeAcademiqueRepo anneeAcademiqueRepo;
    @Autowired
    private ModuleRepo moduleRepo;
    @Autowired
    private NoteRepo noteRepo;
    @Autowired
    private EtudiantRepo etudiantRepo;
    @Autowired
    private ParcoursRepo parcoursRepo;
    @Autowired
    private InscriptionRepo inscriptionRepo;

    //  Ajouter une Evaluation
    @PostMapping("/addEvaluation")
    public ResponseEntity<Evaluation> saveEvaluation(@RequestBody Evaluation evaluation){

        if (evaluation == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        if (evaluation.getCode().equals("EE")){
            evaluation.setIsExam(true);
            return new ResponseEntity<>(evaluationRepo.save(evaluation), HttpStatus.OK);
        }
        return new ResponseEntity<>(evaluationRepo.save(evaluation), HttpStatus.OK);
    }

    //    Liste des Evaluations
    @GetMapping("/findAllEvaluation")
    public ResponseEntity<List<Evaluation>> getAllEvaluation() {

        List<Evaluation> list = evaluationRepo.findAll();
        if (list == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    //    Une Evaluation par id
    @GetMapping("/findEvaluationById/{id}")
    public ResponseEntity<Evaluation> getEvaluationById(@PathVariable("id") Long id) {

        Evaluation evaluation = evaluationRepo.findById(id).orElse(null);
        if (evaluation == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(evaluation, HttpStatus.OK);
    }

    //    Une Evaluation par le code de l'Evaluation
    @GetMapping("/findEvaluationByCode")
    public ResponseEntity<Optional<Evaluation>> getEvaluationByCode(@RequestParam CodeEva code){

        Optional<Evaluation> evaluation = evaluationRepo.findByCode(code);
        if (!evaluation.isPresent()){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(evaluation, HttpStatus.OK);
    }

//    Pourcentage des etudiants ayant valides une evaluation d'un module au cours d'une annee academique
    @GetMapping("/findPourcentageFromEvaluation/{code}/anneeAca/{year}/module")
    public ResponseEntity<Integer> getPourcentageFromEvaluation(@PathVariable String code, @PathVariable int year, @RequestParam("code") String codeModule){

        int pourcentage = 0;
        List<Note> noteList = new ArrayList<>();
        Module module = moduleRepo.findByCode(codeModule).get();
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);

        int nombreTotalEtudiant = 0;

        for (Note note : noteRepo.findAll()){
            Evaluation evaluer = evaluationRepo.findById(note.getEvaluation().getId()).get();
            AnneeAcademique anneeAca = anneeAcademiqueRepo.findById(note.getAnneeAcademique().getId()).get();
            Module mod = moduleRepo.findById(note.getModule().getId()).get();
            if ((evaluer.getCode().equals(code))
                    && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                    && (mod.getCode().equals(module.getCode()))
                    && (note.getIsFinal().equals(false))){
                noteList.add(note);
            }
        }

        int nombreEtudiantValid = 0;
        for (Etudiant etudiant : etudiantRepo.findAll()){
            double noteEtudiant = 0.0;
            for (Note note : noteList) {
                Etudiant etud = etudiantRepo.findById(note.getEtudiant().getId()).get();
                if (etud.getMatricule().equals(etudiant.getMatricule())) {
                    noteEtudiant = note.getValeur();
                    ++nombreTotalEtudiant;
                    if (noteEtudiant >= 10){
                        ++nombreEtudiantValid;
                    }
                }
            }

        }
        pourcentage = (nombreEtudiantValid*100)/nombreTotalEtudiant;
        return new ResponseEntity<>(pourcentage, HttpStatus.OK);
    }

    //    Pourcentage des etudiants ayant valides une evaluation d'un cours au cours d'une annee academique pour une session
    @GetMapping("/findPourcentageFromEvaluation/{code}/anneeAca/{year}/session/{session}/cours")
    public ResponseEntity<Integer> getPourcentageFromEvaluationCours(@PathVariable String code, @PathVariable int year, @PathVariable int session, @RequestParam("code") String codeCours){

        int pourcentage = 0;
        List<Note> noteList = new ArrayList<>();
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        Cours cours = coursRepo.findByCode(codeCours).get();

        int nombreTotalEtudiant = 0;

        for (Note note : noteRepo.findAll()){
            Evaluation evaluer = evaluationRepo.findById(note.getEvaluation().getId()).get();
            AnneeAcademique anneeAca = anneeAcademiqueRepo.findById(note.getAnneeAcademique().getId()).get();
            Cours cour = coursRepo.findById(note.getCours().getCoursId()).get();
            if ((evaluer.getCode().equals(code))
                    && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                    && (cour.getCode().equals(cours.getCode()))
                    && (note.getSessions().equals(session))
                    && (note.getIsFinal().equals(true))){
                noteList.add(note);
            }
        }
        int nombreEtudiantValid = 0;
        for (Etudiant etudiant : etudiantRepo.findAll()){
            double noteEtudiant = 0.0;
            for (Note note : noteList) {
                Etudiant etud = etudiantRepo.findById(note.getEtudiant().getId()).get();
                if (etud.getMatricule().equals(etudiant.getMatricule())) {
                    noteEtudiant = note.getValeur();
                    nombreTotalEtudiant++;
                    if (noteEtudiant >= 10){
                        nombreEtudiantValid++;
                    }
                }
            }

        }
        pourcentage = (nombreEtudiantValid*100)/nombreTotalEtudiant;
        return new ResponseEntity<>(pourcentage, HttpStatus.OK);
    }

   //    Modifier une Evaluation
    @PutMapping("/updateEvaluation/{id}")
    public ResponseEntity<Evaluation> updateEvaluation(@PathVariable("id") Long id, @RequestBody Evaluation evaluation){

        Evaluation evaluationFromDb = evaluationRepo.findById(id).orElse(null);
        if (evaluationFromDb == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        evaluationFromDb.setCode(evaluation.getCode());
        evaluationFromDb.setDescription(evaluation.getDescription());
        evaluationFromDb.setIsExam(evaluation.getIsExam());

        return new ResponseEntity<>(evaluationRepo.save(evaluationFromDb), HttpStatus.OK);
    }

    //    Supprimer une Evaluation
    @DeleteMapping("/deleteEvaluation/{id}")
    public ResponseEntity<String> deleteEvaluation(@PathVariable("id") Long id){
        evaluationRepo.deleteById(id);
        return new ResponseEntity<>("Deleted with Successfully from database", HttpStatus.OK);
    }

}
