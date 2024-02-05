package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.CodeEva;
import com.api.gestnotesapi.entities.Evaluation;
import com.api.gestnotesapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluationService {

    private EvaluationRepo evaluationRepo;
    private CoursRepo coursRepo;
    private AnneeAcademiqueRepo anneeAcademiqueRepo;
    private ModuleRepo moduleRepo;
    private NoteRepo noteRepo;
    private EtudiantRepo etudiantRepo;
    private ParcoursRepo parcoursRepo;
    private InscriptionRepo inscriptionRepo;

    @Autowired
    public EvaluationService(EvaluationRepo evaluationRepo, CoursRepo coursRepo, AnneeAcademiqueRepo anneeAcademiqueRepo, ModuleRepo moduleRepo, NoteRepo noteRepo, EtudiantRepo etudiantRepo, ParcoursRepo parcoursRepo, InscriptionRepo inscriptionRepo) {
        this.evaluationRepo = evaluationRepo;
        this.coursRepo = coursRepo;
        this.anneeAcademiqueRepo = anneeAcademiqueRepo;
        this.moduleRepo = moduleRepo;
        this.noteRepo = noteRepo;
        this.etudiantRepo = etudiantRepo;
        this.parcoursRepo = parcoursRepo;
        this.inscriptionRepo = inscriptionRepo;
    }

    public Evaluation addEvaluation(Evaluation evaluation) {
        if (evaluation == null){
            return null;
        }
        if (evaluation.getCode().equals("EE")){
            evaluation.setIsExam(true);
            return evaluationRepo.save(evaluation);
        }
        return evaluationRepo.save(evaluation);
    }

    public List<Evaluation> getAll() {
        List<Evaluation> list = evaluationRepo.findAll();
        if (list == null){
            return null;
        }
        return list;
    }

    public Evaluation getById(Long id) {
        Evaluation evaluation = evaluationRepo.findById(id).orElse(null);
        if (evaluation == null){
            return null;
        }
        return evaluation;
    }

    public Evaluation getByCode(CodeEva code) {
        Evaluation evaluation = evaluationRepo.findByCode(code).orElse(null);
        if (evaluation == null){
            return null;
        }
        return evaluation;
    }

    public Evaluation update(Long id, Evaluation evaluation) {
        Evaluation update = getById(id);
        if (update == null){
            return null;
        }
        update.setCode(evaluation.getCode());
        update.setDescription(evaluation.getDescription());
        update.setIsExam(evaluation.getIsExam());

        return evaluationRepo.save(update);
    }

    public void delete(Long id) {
        evaluationRepo.deleteById(id);
    }
}
