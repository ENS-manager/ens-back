package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParcoursService {

    private ParcoursRepo parcoursRepo;
    private NiveauService niveauServicee;
    private OptionService optionService;
    private EtudiantRepo etudiantRepo;
    private AnneeAcademiqueService anneeAcademiqueService;
    private InscriptionService inscriptionService;
    private DepartementService departementService;
    private CoursService coursService;


    @Autowired
    public ParcoursService(ParcoursRepo parcoursRepo, NiveauService niveauServicee, DepartementService departementService, OptionService optionService, EtudiantRepo etudiantRepo, AnneeAcademiqueService anneeAcademiqueService, InscriptionService inscriptionService, CoursService coursService) {
        this.optionService = optionService;
        this.etudiantRepo = etudiantRepo;
        this.anneeAcademiqueService = anneeAcademiqueService;
        this.inscriptionService = inscriptionService;
        this.niveauServicee = niveauServicee;
        this.parcoursRepo = parcoursRepo;
        this.departementService = departementService;
        this.coursService = coursService;
    }

    public Parcours getParcoursByOptionAndNiveau(int level, String code){

        Option option = optionService.getByCode(code);
        Niveau niveau = niveauServicee.getByValeur(level);

        if (option == null || niveau == null) {
            return null;
        }

        return parcoursRepo.findByOptionAndNiveau(option, niveau);
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
        if (parcours.getLabel() != null){
            return parcoursRepo.save(parcours);
        }
        Niveau niveau = niveauServicee.getById(parcours.getNiveau().getId());
        Option option = optionService.getById( parcours.getOption().getId());
        String code = option.getCode();
        int valeur = niveau.getValeur();
        String label = code+" "+valeur;
        parcours.setLabel(label);
        return parcoursRepo.save(parcours);
    }

    public List<Parcours> getParcoursCours(String code){
        Cours cours = coursService.getByCode(code);
        if (cours == null){
            return null;
        }
        Departement departement = departementService.getByCode(cours.getDepartement().getCode());
        if (departement == null){
            return null;
        }
        Option option = departement.getOptions().get(0);
        List<Parcours> parcours = parcoursRepo.findAllByOption(option);
        if (parcours == null){
            return null;
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
}
