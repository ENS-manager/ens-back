package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.entities.Module;
import com.api.gestnotesapi.services.EvaluationService;
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

    private EvaluationService evaluationService;

    @Autowired
    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }


    //  Ajouter une Evaluation
    @PostMapping("/addEvaluation")
    public ResponseEntity<Evaluation> saveEvaluation(@RequestBody Evaluation evaluation){
        Evaluation update = evaluationService.addEvaluation(evaluation);
        if (update == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    //    Liste des Evaluations
    @GetMapping("/findAllEvaluation")
    public ResponseEntity<List<Evaluation>> getAllEvaluation() {

        List<Evaluation> list = evaluationService.getAll();
        if (list == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    //    Une Evaluation par id
    @GetMapping("/findEvaluationById/{id}")
    public ResponseEntity<Evaluation> getEvaluationById(@PathVariable("id") Long id) {

        Evaluation evaluation = evaluationService.getById(id);
        if (evaluation == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(evaluation, HttpStatus.OK);
    }

    //    Une Evaluation par le code de l'Evaluation
    @GetMapping("/findEvaluationByCode")
    public ResponseEntity<Evaluation> getEvaluationByCode(@RequestParam CodeEva code){

        Evaluation evaluation = evaluationService.getByCode(code);
        if (evaluation == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(evaluation, HttpStatus.OK);
    }

//    A faire
//    Pourcentage des etudiants ayant valides une evaluation d'un module au cours d'une annee academique
    @GetMapping("/findPourcentageFromEvaluation/{code}/anneeAca/{year}/module")
    public ResponseEntity<Integer> getPourcentageFromEvaluation(@PathVariable String code, @PathVariable int year, @RequestParam("code") String codeModule){

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

//    A faire
    //    Pourcentage des etudiants ayant valides une evaluation d'un cours au cours d'une annee academique pour une session
    @GetMapping("/findPourcentageFromEvaluation/{code}/anneeAca/{year}/session/{session}/cours")
    public ResponseEntity<Integer> getPourcentageFromEvaluationCours(@PathVariable String code, @PathVariable int year, @PathVariable int session, @RequestParam("code") String codeCours){

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

   //    Modifier une Evaluation
    @PutMapping("/updateEvaluation/{id}")
    public ResponseEntity<Evaluation> updateEvaluation(@PathVariable("id") Long id, @RequestBody Evaluation evaluation){

        Evaluation evaluationFromDb = evaluationService.update(id, evaluation);
        if (evaluationFromDb == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(evaluationFromDb, HttpStatus.OK);
    }

    //    Supprimer une Evaluation
    @PutMapping("/deleteEvaluation/{id}")
    public ResponseEntity<String> deleteEvaluation(@PathVariable("id") Long id){
        return new ResponseEntity<>(evaluationService.delete(id), HttpStatus.OK);
    }

}
