package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParcoursService {

    private ParcoursRepo parcoursRepo;
    private OptionService optionService;
    private EtudiantRepo etudiantRepo;
    private AnneeAcademiqueService anneeAcademiqueService;
    private InscriptionService inscriptionService;
    private DepartementService departementService;
    private CoursRepo coursRepo;
    private CycleRepo cycleRepo;
    private NiveauRepo niveauRepo;


    @Autowired
    public ParcoursService(ParcoursRepo parcoursRepo, DepartementService departementService, OptionService optionService, EtudiantRepo etudiantRepo, AnneeAcademiqueService anneeAcademiqueService, InscriptionService inscriptionService,  CoursRepo coursRepo, CycleRepo cycleRepo, NiveauRepo niveauRepo) {
        this.optionService = optionService;
        this.etudiantRepo = etudiantRepo;
        this.anneeAcademiqueService = anneeAcademiqueService;
        this.inscriptionService = inscriptionService;
        this.parcoursRepo = parcoursRepo;
        this.departementService = departementService;
        this.coursRepo = coursRepo;
        this.cycleRepo = cycleRepo;
        this.niveauRepo = niveauRepo;
    }

    public Parcours getParcoursByOptionAndNiveau(int level, String code){

        Option option = optionService.getByCode(code);
        Niveau niveau = niveauRepo.findByValeur(level).orElse(null);

        if (option == null || niveau == null) {
            return null;
        }
        Parcours parcours = parcoursRepo.findByOptionAndNiveau(option, niveau).orElse(null);
        if (parcours == null){
            return null;
        }
        return parcours;
    }

    public Parcours getParcoursEtudiant(Long id, String anneeAca){
        Etudiant etudiant = etudiantRepo.findById(id).orElse(null);
        AnneeAcademique anneeAcademique = anneeAcademiqueService.getByCode(anneeAca);
        if (etudiant == null || anneeAcademique == null){
            return null;
        }
        return inscriptionService.getByEtudiantAndAnneeAcademique(etudiant, anneeAcademique).getParcours();
    }

    public Parcours getByLabel(String label) {
        Parcours parcours = parcoursRepo.findByLabel(label).orElse(null);
        if (parcours == null){
            return null;
        }
        return parcours;
    }

    public Parcours addParcours(Parcours parcours) {
        if (parcours == null){
            return null;
        }
        Parcours newParcours = new Parcours();
        newParcours.setLabel(parcours.getLabel());
        newParcours.setNiveau(parcours.getNiveau());
        newParcours.setOption(parcours.getOption());
        newParcours.getCours()
                .addAll(parcours
                        .getCours()
                        .stream()
                        .map(cours -> {
                            Cours newCours = coursRepo.findByCoursId(cours.getCoursId());
                            newCours.getParcours().add(newParcours);
                            return newCours;
                        }).collect(Collectors.toList()));
        if (parcours.getLabel() != null){
            return parcoursRepo.save(newParcours);
        }
        Niveau niveau = niveauRepo.findById(parcours.getNiveau().getId()).orElse(null);
        if (niveau == null){
            return null;
        }
        Option option = optionService.getById( parcours.getOption().getId());
        String code = option.getCode();
        int valeur = niveau.getValeur();
        String label = code+" "+valeur;
        newParcours.setLabel(label);
        return parcoursRepo.save(newParcours);
    }

    public List<Parcours> getParcoursCours(String code){
        Cours cours = coursRepo.findByCode(code).orElse(null);
        if (cours == null){
            return null;
        }
        List<Parcours> parcours = new ArrayList<>();
        for (Parcours par : parcoursRepo.findAll()){
            if (par.getCours().contains(cours)){
                parcours.add(par);
            }
        }
        return parcours;
    }

    public List<Parcours> getAll() {
        List<Parcours> list = parcoursRepo.findAll();
        if (list == null){
            return null;
        }
        return list;
    }

    public Parcours getById(Long id) {
        Parcours parcours = parcoursRepo.findById(id).orElse(null);
        if (parcours == null){
            return null;
        }
        return parcours;
    }

    public List<Parcours> getAllByDepartement(String code) {
        Departement departement = departementService.getByCode(code);
        if (departement == null){
            return null;
        }
        List<Parcours> parcoursList = new ArrayList<>();
        for (Parcours parcours : parcoursRepo.findAll()){
            Option option = optionService.getById(parcours.getOption().getId());
            Departement depart = departementService.getById(option.getDepartement().getId());
            if (depart.getCode().equals(departement.getCode())){
                parcoursList.add(parcours);
            }
        }
        if (parcoursList == null){
            return null;
        }
        return parcoursList;
    }

    public String delete(Long id) {
        Parcours parcours = getById(id);
        if (parcours == null){
            return "Aucun objet trouve pour l'id specifie";
        }
        parcours.setActive(false);
        parcoursRepo.save(parcours);

        return "Operation reussi avec succes";
    }

    public Parcours getByLabelAndActive(String label) {
        Parcours parcours = parcoursRepo.findByLabelAndActive(label, true);
        if (parcours == null){
            return null;
        }
        return parcours;
    }

    public Parcours getOneParcoursOfCours(String code){
        Cours cours = coursRepo.findByCode(code).orElse(null);
        if (cours == null){
            return null;
        }
        List<Parcours> parcoursList = getAll();
        if (parcoursList == null){
            return null;
        }
        Parcours parcours = new Parcours();
        for (Parcours par : parcoursList){
            if (par.getCours().contains(cours)){
                parcours = par;
            }
        }
        return parcours;
    }

    //    pour le pv grand Jury
    public List<Parcours> getListParcoursByOptionAndCycle(String code, int value){
        Option option = optionService.getByCode(code);
        Cycle cycle = cycleRepo.findByValeur(value);
        if (option == null || cycle == null){
            return null;
        }
        List<Parcours> parcoursList = new ArrayList<>();
        List<Niveau> niveauList = niveauRepo.findAllByCycle(cycle);
        for (Niveau niveau : niveauList){
            parcoursList.add(getParcoursByOptionAndNiveau(niveau.getValeur(), option.getCode()));
        }

        return parcoursList;
    }

//    //    pour le pv grand Jury
//    public List<Parcours> getListParcoursByOption(String code){
//        Option option = optionService.getByCode(code);
//        if (option == null){
//            return null;
//        }
//        List<Parcours> parcoursList = parcoursRepo.findAllByOption(option);
//        return parcoursList;
//    }
}
