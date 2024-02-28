package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoursService {

    private OptionRepo optionRepo;
    private CoursRepo coursRepo;
    private NiveauRepo niveauRepo;
    private SemestreRepo semestreRepo;
    private DepartementRepo departementRepo;
    private CycleRepo cycleRepo;
    private ParcoursService parcoursService;
    private EnseignantRepo enseignantRepo;

    @Autowired
    public CoursService(OptionRepo optionRepo, CoursRepo coursRepo, NiveauRepo niveauRepo, SemestreRepo semestreRepo, DepartementRepo departementRepo, CycleRepo cycleRepo, ParcoursService parcoursService, EnseignantRepo enseignantRepo) {
        this.optionRepo = optionRepo;
        this.coursRepo = coursRepo;
        this.niveauRepo = niveauRepo;
        this.semestreRepo = semestreRepo;
        this.departementRepo = departementRepo;
        this.cycleRepo = cycleRepo;
        this.parcoursService = parcoursService;
        this.enseignantRepo = enseignantRepo;
    }

    public List<Cours> getListCoursByParcours(String label){
        List<Cours> coursList = new ArrayList<>();
        Parcours parcours = parcoursService.getByLabel(label);
        if (parcours == null){
            return null;
        }
        for (Cours cours : getAll()){
            if (cours.getParcours().contains(parcours)){
                coursList.add(cours);
            }
        }
        return coursList;
    }

//    Liste des cours d'une option
    public List<Cours> getListCoursByOptionAndCycle(String code, int value){
        Option option = optionRepo.findByCode(code).orElse(null);
        Cycle cycle = cycleRepo.findByValeur(value);
        if (option == null || cycle == null){
            return null;
        }
        List<Parcours> parcoursList = parcoursService.getListParcoursByOptionAndCycle(code, value);
        if (parcoursList == null){
            return null;
        }
        List<Cours> coursList = new ArrayList<>();
        for (Parcours parcours : parcoursList){
            if (parcours != null){
                coursList.addAll(getListCoursByParcours(parcours.getLabel()));
            }
        }
        return coursList;
    }

    public Cours addCours(Cours cours) {
        if (cours == null){
            return null;
        }
        if (cours.getIsStage().equals(true)){
            Cours newCours = new Cours();
            newCours.setCode("Stage");
            newCours.setTypecours(cours.getTypecours());
            newCours.setSemestre(cours.getSemestre());
            newCours.setIntitule(cours.getIntitule());
            newCours.setCredit(cours.getCredit());
            newCours.setNatureUE(cours.getNatureUE());
            newCours.setIsStage(cours.getIsStage());
            newCours.getParcours()
                    .addAll(cours
                            .getParcours()
                            .stream()
                            .map(parcours -> {
                                Parcours newParcours = parcoursService.getById(parcours.getId());
                                newParcours.getCours().add(newCours);
                                return newParcours;
                            }).collect(Collectors.toList()));
            return coursRepo.save(cours);
        }
        Cours newCours = new Cours();
        newCours.setTypecours(cours.getTypecours());
        newCours.setSemestre(cours.getSemestre());
        newCours.setIntitule(cours.getIntitule());
        newCours.setCredit(cours.getCredit());
        newCours.setNatureUE(cours.getNatureUE());
        newCours.setIsStage(cours.getIsStage());
        newCours.getParcours()
                .addAll(cours
                        .getParcours()
                        .stream()
                        .map(parcours -> {
                            Parcours newParcours = parcoursService.getById(parcours.getId());
                            newParcours.getCours().add(newCours);
                            return newParcours;
                        }).collect(Collectors.toList()));
        if (cours.getCode() != null){
            newCours.setCode(cours.getCode());
            return coursRepo.save(newCours);
        }
        Semestre semestre = semestreRepo.findById(cours.getSemestre().getId()).orElse(null);
        if (semestre == null){
            return null;
        }

        Parcours parcours = new Parcours();
        for (Parcours par : cours.getParcours()){
            parcours = par;
        }
        if (parcours == null){
            return null;
        }
        Option option = optionRepo.findById(parcours.getOption().getId()).orElse(null);
        if (option == null){
            return null;
        }
        Departement departement = departementRepo.findById(option.getDepartement().getId()).orElse(null);
        if (departement == null){
            return null;
        }
        Niveau niveau = niveauRepo.findById(parcours.getNiveau().getId()).orElse(null);
        String codeDepart = departement.getCode();
        int valeurNiveau = niveau.getValeur();
        int valeurSemestre = semestre.getValeur();
        Cours updateCours = coursRepo.save(newCours);
        String code = codeDepart+valeurNiveau+updateCours.getCoursId()+valeurSemestre;
        updateCours.setCode(code);

        return coursRepo.save(updateCours);
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
        List<Cours> coursList = new ArrayList<>();
        List<Parcours> parcoursList = parcoursService.getAllByDepartement(departement.getCode());
        if (parcoursList == null){
            return null;
        }
        for (Parcours parcours : parcoursList){
            coursList.addAll(getListCoursByParcours(parcours.getLabel()));
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
        update.setIntitule(cours.getIntitule());
        update.setNatureUE(cours.getNatureUE());
        update.setTypecours(cours.getTypecours());
        update.setSemestre(cours.getSemestre());

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

    public String generateCode(NatureUE natureUE, int semestre, int niveau){
        String nature = "";
        if (natureUE.equals(NatureUE.Complementaire)){
            nature = "C";
        }else if (natureUE.equals(NatureUE.Fondamentale)){
            nature = "F";
        }else {
            nature = "P";
        }
        String code = "UE"+nature+niveau+"0"+semestre;
        return code;
    }

    public List<Cours> getListCoursFromTeacher(Long id){
        Enseignant enseignant = enseignantRepo.findById(id).orElse(null);
        if (enseignant == null){
            return null;
        }
        return enseignant.getCours();
    }
}
