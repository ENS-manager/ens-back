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
    private DepartementRepo departementRepo;

    @Autowired
    public CoursService(OptionRepo optionRepo, CoursRepo coursRepo, NiveauRepo niveauRepo, SemestreRepo semestreRepo, ParcoursRepo parcoursRepo, DepartementRepo departementRepo) {
        this.optionRepo = optionRepo;
        this.coursRepo = coursRepo;
        this.niveauRepo = niveauRepo;
        this.semestreRepo = semestreRepo;
        this.parcoursRepo = parcoursRepo;
        this.departementRepo = departementRepo;
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

    public Cours addCours(Cours cours) {
        if (cours == null){
            return null;
        }
        if (cours.getCode() != null){
            return coursRepo.save(cours);
        }
        Semestre semestre = semestreRepo.findById(cours.getSemestre().getId()).orElse(null);
        Departement departement = departementRepo.findById(cours.getDepartement().getId()).orElse(null);
        if (semestre == null || departement == null){
            return null;
        }
        Niveau niveau = niveauRepo.findById(semestre.getNiveau().getId()).orElse(null);
        String codeDepart = departement.getCode();
        int valeurNiveau = niveau.getValeur();
        int valeurSemestre = semestre.getValeur();
        Cours updateCours = coursRepo.save(cours);
        String code = codeDepart+valeurNiveau+updateCours.getCoursId()+valeurSemestre;
        cours.setCode(code);

        return coursRepo.save(cours);
    }

    public List<Cours> getAll() {
        List<Cours> coursList = coursRepo.findAll();
        if (coursList == null){
            return null;
        }
        return coursList;
    }

    public Cours getById(Long id) {
        Cours cours = coursRepo.findByCoursId(id);
        if (cours == null){
            return null;
        }
        return cours;
    }

    public Cours getByCode(String code) {
        Cours cours = coursRepo.findByCode(code).orElse(null);
        if (cours == null){
            return null;
        }
        return cours;
    }

    public List<Cours> getAllCoursByDepartement(String codeDepart) {

        Departement departement = departementRepo.findByCode(codeDepart).orElse(null);
        if (departement == null){
            return null;
        }
        List<Cours> coursList = coursRepo.findAllByDepartement(departement);
        if (coursList == null){
            return null;
        }
        return coursList;
    }

    public Cours update(Long id, Cours cours) {
        Cours update = coursRepo.findByCoursId(id);
        if (update == null){
            return null;
        }
        update.setCode(cours.getCode());
        update.setCredit(cours.getCredit());
        update.setDepartement(cours.getDepartement());
        update.setIntitule(cours.getIntitule());
        update.setNatureUE(cours.getNatureUE());
        update.setTypecours(cours.getTypecours());

        return coursRepo.save(update);
    }

    public String delete(Long id) {
        Cours cours = getById(id);
        if (cours == null){
            return "Aucun objet trouve pour l'id specifie";
        }
        cours.setActive(false);
        coursRepo.save(cours);

        return "Operation reussi avec succes";
    }
}
