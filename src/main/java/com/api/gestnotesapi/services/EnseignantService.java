package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.AnneeAcademique;
import com.api.gestnotesapi.entities.Cours;
import com.api.gestnotesapi.entities.Enseignant;
import com.api.gestnotesapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnseignantService {

    private EnseignantRepo enseignantRepo;
    private CoursRepo coursRepo;
    private DepartementRepo departementRepo;
    private ParcoursRepo parcoursRepo;
    private OptionRepo optionRepo;

    @Autowired
    public EnseignantService(EnseignantRepo enseignantRepo, CoursRepo coursRepo, DepartementRepo departementRepo, ParcoursRepo parcoursRepo, OptionRepo optionRepo) {
        this.enseignantRepo = enseignantRepo;
        this.coursRepo = coursRepo;
        this.departementRepo = departementRepo;
        this.parcoursRepo = parcoursRepo;
        this.optionRepo = optionRepo;
    }

    public Enseignant addEnseignant(Enseignant enseignant) {
        if (enseignant == null){
            return null;
        }
        Enseignant newEnseignant = new Enseignant();
        newEnseignant.setNom(enseignant.getNom());
        newEnseignant.getCours()
                .addAll(enseignant
                        .getCours()
                        .stream()
                        .map(cours -> {
                            Cours newCours = coursRepo.findByCoursId(cours.getCoursId());
                            newCours.getEnseignant().add(newEnseignant);
                            return newCours;
                        }).collect(Collectors.toList()));
        return enseignantRepo.save(newEnseignant);
    }

    public List<Enseignant> getAll() {
        List<Enseignant> list = enseignantRepo.findAll();
        if (list == null){
            return null;
        }
        return list;
    }

    public Enseignant getById(Long id) {
        Enseignant enseignant = enseignantRepo.findById(id).orElse(null);
        if (enseignant == null){
            return null;
        }
        return enseignant;
    }

    public Enseignant update(Long id, Enseignant enseignant) {
        Enseignant update = getById(id);
        if (update == null){
            return null;
        }
        update.setNom(enseignant.getNom());
        update.setCours(enseignant.getCours());
        return enseignantRepo.save(update);
    }

    public String delete(Long id) {
        Enseignant enseignant = getById(id);
        if (enseignant == null){
            return "Aucun objet trouve pour l'id specifie";
        }
        enseignant.setActive(false);
        enseignantRepo.save(enseignant);

        return "Operation reussi avec succes";
    }
}
