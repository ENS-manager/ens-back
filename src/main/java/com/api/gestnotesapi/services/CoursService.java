package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CoursService {

    private OptionRepo optionRepo;
    private CoursRepo coursRepo;
    private NiveauRepo niveauRepo;
    private SemestreRepo semestreRepo;
    private ParcoursRepo parcoursRepo;

    @Autowired
    public CoursService(OptionRepo optionRepo, CoursRepo coursRepo, NiveauRepo niveauRepo, SemestreRepo semestreRepo, ParcoursRepo parcoursRepo) {
        this.optionRepo = optionRepo;
        this.coursRepo = coursRepo;
        this.niveauRepo = niveauRepo;
        this.semestreRepo = semestreRepo;
        this.parcoursRepo = parcoursRepo;
    }

    public List<Cours> getListCoursByParcours(String label){
        List<Cours> coursList = new ArrayList<>();
        Optional<Parcours> parcours = parcoursRepo.findByLabel(label);
        if (!parcours.isPresent()){
            return null;
        }
        for (Cours cours : coursRepo.findAll()){
            Semestre semestre = semestreRepo.findById(cours.getSemestre().getId()).get();
            Niveau niveau = niveauRepo.findById(semestre.getNiveau().getId()).get();
            for (Option option : optionRepo.findAll()){
                Parcours par = parcoursRepo.findByOptionAndNiveau(option, niveau);
                if (par.getLabel().equals(parcours.get().getLabel())){
                    coursList.add(cours);
                }
            }
        }
        return coursList;
    }
}
